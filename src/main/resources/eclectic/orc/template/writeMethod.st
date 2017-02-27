writeMethod(schemaType, sclass) ::= <<
    protected void write(<sclass.name> datum) {
        <writeStruct(schemaType)>
    }
>>