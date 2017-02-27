columnChar(schemaType) ::= <<
    byte[] charArray = new byte[1];
    charArray[0] = datum<schemaType.propertyAccess>;
    vector.setVal(vectorizedRowBatch.size, charArray);
>>