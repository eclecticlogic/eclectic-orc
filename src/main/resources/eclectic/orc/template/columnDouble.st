columnDouble(schemaType) ::= <<
    vector.vector[vectorizedRowBatch.size] = datum<schemaType.propertyAccess>;
>>