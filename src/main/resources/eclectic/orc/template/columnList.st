columnList(schemaType) ::= <<
    java.util.List list = datum<schemaType.propertyAccess>;
    vector.offsets[vectorizedRowbatch.size] = vector.childCount;
    vector.lengths[batch.size] = list.size();
    vector.childCount += list.size();
    for (int i = 0; i < values.size(); i++) {
        int loc = vector.offsets[batch.size] + i;
        vector.child.vector[loc] = values[i];
    }
>>