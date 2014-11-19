#!/bin/sh
mvnDebug clean package -DskipTests -Djetty.port=8082 hpi:run