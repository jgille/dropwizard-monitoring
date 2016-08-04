#!/bin/bash

PROJECT_DIR=$PWD
MONITORED_SERVICE=monitored-service
MONITOR=monitor
BLACKBOX_TESTS=monitoring-blackbox-tests

start () {
    stop
    build
    start_services
}

run_tests () {
    echo "Running black box tests"

    docker stop ${BLACKBOX_TESTS} &> /dev/null
    docker rm ${BLACKBOX_TESTS} &> /dev/null
    docker run --name ${BLACKBOX_TESTS} --link ${MONITORED_SERVICE}:${MONITORED_SERVICE} ${BLACKBOX_TESTS}:${VERSION}

    status=$?
    if [ ${status} != 0 ]
    then
        echo "Black box tests failed"
        exit ${status}
    fi
}

start_services () {
    cd ${PROJECT_DIR}
    docker run -d -p 8067:8067 -p 9067:9067 --name ${MONITOR} ${MONITOR}:${VERSION}
    docker run -d -p 8066:8066 -p 9066:9066 --name ${MONITORED_SERVICE} --link ${MONITOR}:${MONITOR} ${MONITORED_SERVICE}:${VERSION}

    wait_for "localhost:9067"
    wait_for "localhost:9066"
}

wait_for () {
    timeout=30;
    date1=$((`date +%s` + $timeout));
    echo "Waiting for $1"
    status=$(get $1)
    while [ ${status} -ne 200 -a  "$date1" -ne `date +%s` ]
    do
        echo "$(date -u --date @$(($date1 - `date +%s` )) +%H:%M:%S)";
        sleep 1
        status=$(get $1)
    done

    if [ ${status} -eq 200 ]; then
        echo "$1 is up!"
    else
        echo "Timed out waiting for $1"
        exit 1
    fi
}

get () {
    curl -s -o /dev/null -w "%{http_code}" $1
}

stop() {
    # Only stop/remove what we start
    for container_name in ${MONITORED_SERVICE} ${MONITOR}
    do
        ps=$(docker ps -a|grep ${container_name})
        if [ -n "$ps" ]; then
            docker stop ${container_name} &> /dev/null
            docker rm ${container_name} &> /dev/null
        fi
    done
}

build() {
    cd ${PROJECT_DIR}
    docker build -t ${MONITORED_SERVICE}:${VERSION} -f ${PROJECT_DIR}/sample-app/Dockerfile ${PROJECT_DIR}/sample-app
    docker build -t ${MONITOR}:${VERSION} -f ${PROJECT_DIR}/sample-monitor/Dockerfile ${PROJECT_DIR}/sample-monitor
    docker build -t ${BLACKBOX_TESTS}:${VERSION} -f ${PROJECT_DIR}/monitoring-blackbox-tests/Dockerfile ${PROJECT_DIR}/monitoring-blackbox-tests
}

get_logs () {
    mkdir -p ${PROJECT_DIR}/logs
    for container_name in ${MONITORED_SERVICE} ${MONITOR}
    do
        ps=$(docker ps -a|grep ${container_name})
        if [ -n "$ps" ]; then
            docker logs ${container_name} > ${PROJECT_DIR}/logs/${container_name}.log 2> /dev/null
        fi
    done
}

if [ -n "$2" ];
then
    echo "using version: '$2'"
    VERSION=$2;
else
    echo "using default version latest"
    VERSION=latest;
fi

case "$1" in
    up)
        start
        ;;
    down)
        stop
        ;;
    build)
        build
        ;;
    test)
        run_tests
        ;;
    verify)
        start
        run_tests
        ;;
    get_logs)
        get_logs
        ;;

    *)
        echo "USAGE: ./services.sh up|down|build|test|verify|get_logs"
esac
