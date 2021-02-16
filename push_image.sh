#!/bin/sh

export VERSION=$(git rev-parse --short "$GITHUB_SHA")
export IMAGE=ferlabcrsj/fhir-console:$VERSION
export LATEST_IMAGE=ferlabcrsj/fhir-console:latest

docker build -t $IMAGE .
docker tag $IMAGE $LATEST_IMAGE

docker push $IMAGE
docker push $LATEST_IMAGE
