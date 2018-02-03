# Solr timestampprice function query

Solr query function that retrieve price (float value) according to current / entered unix timestamp.

## Data format
Function expects data in the following format:
```
date_from|price[ date_from|price ...]
```

In example:
```
1483228800|100.00 1483239600|63.30 1483272000|100.00
```

## Installation
1. Compile project:
    
    ```bash
    mvn clean install
    ```

2. Copy generated jar file to Solr `contrib` folder:
    ```
    cp target/solr-timestampprice-*.jar <SOLR_INSTALL_DIR>/contrib
    ``` 

3. Add definition in `solrconfig.xml` file:
    ```xml
    <lib dir="${solr.install.dir:../../../..}/contrib" regex="solr\-timestampprice\-.+\.jar" />
    <valueSourceParser name="timestamp_price" class="net.gilek.solr.timestampprice.TimestampPriceValueSourceParser" />
    ```

## Usage
```
timestamp_price(source[, timestamp])
```

Function accepts the following parameters:

| Parameter | Required | Description                   |
| --------- | -------- | ----------------------------- |
| source    | yes      | Source value. It can be name of the field or string type value (in quotation marks). |
| timestamp | no       | Unix timestamp. If empty (default) actual time will be used. |


