writeColumn(schemaType) ::= <<
    <if(schemaType.typeStruct)>
        <writeStruct(schemaType)>
    <else>
        {
            <schemaType.vectorClassName> vector = vectorizedRowBatch.cols[<schemaType.columnIndex>];
            <if(schemaType.primitive)>
                <writeColumnSetter(schemaType)>
            <else>
                if (datum<schemaType.propertyAccess> == null) {
                    setNull(vector);
                } else {
                    <writeColumnSetter(schemaType)>
                }
            <endif>
        }
    <endif>

>>