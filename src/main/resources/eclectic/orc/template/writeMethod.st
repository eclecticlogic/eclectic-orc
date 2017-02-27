writeMethod(schemaType, sclass) ::= <<
    protected void write(Object inputRow) {
        <sclass.name> datum = (<sclass.name>)inputRow;
        <writeStruct(schemaType)>
    }
>>