writeStruct(schemaType) ::= <<
    <schemaType.structChildren:writeColumn()>
>>