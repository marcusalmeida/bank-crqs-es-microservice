.PHONY:

# ==============================================================================
# Docker

local:
	@echo Clearing kafka data
	rm -rf ./data/kafka_data
	@echo Clearing zookeeper data
	rm -rf ./data/zookeeper
	@echo Clearing prometheus data
	rm -rf ./data/prometheus
	@echo Starting local docker compose
	docker-compose -f docker-compose.yaml up -d --build


clean_docker_data:
	@echo Clearing kafka data
	rm -rf ./data/kafka_data
	@echo Clearing zookeeper data
	rm -rf ./data/zookeeper
	@echo Clearing prometheus data
	rm -rf ./data/prometheus
	@echo Clearing pg data
	rm -rf ./data/pgdata
	@echo Clearing mongo data
	rm -rf ./data/mongodb_data_container

# ==============================================================================
# Docker support

FILES := $(shell docker ps -aq)

down-local:
	docker stop $(FILES)
	docker rm $(FILES)

clean:
	docker system prune -f

logs-local:
	docker logs -f $(FILES)