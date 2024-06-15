#!/usr/bin/env sh
docker login --username oauth --password $OAUTH_TOKEN cr.yandex
docker build . -t cr.yandex/$REGISTRY_ID/pnp
docker push cr.yandex/$REGISTRY_ID/pnp