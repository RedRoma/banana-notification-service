#!/usr/bin/env bash

version="1.0-SNAPSHOT"
jar="aroma-notification-service-$version.jar"

nohup java -jar $jar > server.log &