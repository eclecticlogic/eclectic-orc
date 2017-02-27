columnDecimal(schemaType) ::= <<
    vector.set(vectorizedRowBatch.size, org.apache.hadoop.hive.common.type.HiveDecimal.create(datum<schemaType.propertyAccess>, false));
>>