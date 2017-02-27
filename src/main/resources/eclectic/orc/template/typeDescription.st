typeDescription(schemaType) ::= <<
    org.apache.orc.TypeDescription.<schemaType.createCategory>(<if(schemaType.listType)><typeDescription(schemaType.listChild)><endif><if(schemaType.mapType)><typeDescription(schemaType.mapKey)><typeDescription(schemaType.mapValue)><endif>)
            <if(schemaType.length)>.withMaxLength(<schemaType.length>)<endif>
            <if(schemaType.precisionFirst)>.withPrecision(<schemaType.precision>)<endif>
            <if(schemaType.scaleLast)>.withScale(<schemaType.scale>)<endif>
            <if(schemaType.scaleFirst)>.withScale(<schemaType.scale>)<endif>
            <if(schemaType.precisionLast)>.withPrecision(<schemaType.precision>)<endif>
        <if(schemaType.typeStruct)><schemaType.structChildren:fieldSetup()><endif>
>>