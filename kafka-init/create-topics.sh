#!/bin/bash

# Liste des topics à créer
topics=("topic1" "topic2" "topic3")

# Bootstrap server Kafka
bootstrap_server="broker:29092"

# Fonction pour créer un topic
create_topic() {
    local topic=$1
    local exists=$(kafka-topics --describe --topic $topic --bootstrap-server $bootstrap_server 2>/dev/null | grep -c "$topic")
    
    if [ $exists -eq 0 ]; then
        if kafka-topics --create --topic $topic --bootstrap-server $bootstrap_server --replication-factor 1 --partitions 1; then
            echo "Le topic $topic a été créé avec succès."
        else
            echo "ERREUR: Échec de la création du topic $topic"
        fi
    else
        echo "Le topic $topic existe déjà."
    fi
}

# Boucle pour créer les topics
for topic in "${topics[@]}"
do
    create_topic $topic
done
