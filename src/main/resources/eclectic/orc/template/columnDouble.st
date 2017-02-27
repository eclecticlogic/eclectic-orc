columnDouble(schemaType) ::= <<
    column[vectorizedRowBatch.size] = datum<schemaType.propertyAccess>;
>>