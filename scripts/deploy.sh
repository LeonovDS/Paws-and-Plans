#!/usr/bin/env sh
docker login --username oauth --passworf $OAUTH_TOKEN cr.yandex
docker build . -t cr.yandex/$REGISTRY_ID/pnp
docker push cr.yandex/$REGISTRY_ID/pnp