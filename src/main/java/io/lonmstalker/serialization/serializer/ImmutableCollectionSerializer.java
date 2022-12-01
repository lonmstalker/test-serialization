package io.lonmstalker.serialization.serializer;

import org.nustaq.serialization.FSTClazzInfo;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.nustaq.serialization.serializers.FSTCollectionSerializer;
import org.nustaq.serialization.util.FSTUtil;

import java.io.IOException;
import java.util.*;

public class ImmutableCollectionSerializer extends FSTCollectionSerializer {
    public static final Class<?> UNMODIFIABLE_COLLECTION_CLASS = List.of().getClass();
    public static final Class<?> UNMODIFIABLE_RANDOM_ACCESS_LIST_CLASS = List.of(1,2,3).getClass();
    public static final Class<?> UNMODIFIABLE_SET_CLASS = Set.of().getClass();
    public static final Class<?> UNMODIFIABLE_LIST_CLASS = Set.of(1,2,3).getClass();

    public void writeObject(FSTObjectOutput out, Object toWrite, FSTClazzInfo clzInfo, FSTClazzInfo.FSTFieldInfo referencedBy, int streamPosition) throws IOException {
        out.writeObject(clzInfo.getClazz());
        Collection coll = (Collection)toWrite;
        out.writeInt(coll.size());
        Iterator iterator = coll.iterator();

        while(iterator.hasNext()) {
            out.writeObject(iterator.next());
        }

    }

    public Object instantiate(Class objectClass, FSTObjectInput in, FSTClazzInfo serializationInfo, FSTClazzInfo.FSTFieldInfo referencee, int streamPosition) throws Exception {
        Class clazz = (Class)in.readObject();
        int len = in.readInt();

        try {
            if (UNMODIFIABLE_RANDOM_ACCESS_LIST_CLASS == clazz) {
                var res = new ArrayList(len);
                this.fillArray(in, serializationInfo, referencee, streamPosition, res, len);
                return Collections.unmodifiableList(res);
            } else if (UNMODIFIABLE_LIST_CLASS == clazz) {
                var res = new HashSet();
                this.fillArray(in, serializationInfo, referencee, streamPosition, res, len);
                return Collections.unmodifiableSet(res);
            } else if (UNMODIFIABLE_SET_CLASS == clazz) {
                var res = new HashSet(len);
                this.fillArray(in, serializationInfo, referencee, streamPosition, res, len);
                return Collections.unmodifiableSet(res);
            } else if (UNMODIFIABLE_COLLECTION_CLASS == clazz) {
                var res = new ArrayList(len);
                this.fillArray(in, serializationInfo, referencee, streamPosition, res, len);
                return Collections.unmodifiableList(res);
            } else {
                throw new RuntimeException("unexpected class tag " + clazz);
            }
        } catch (Throwable var9) {
            FSTUtil.rethrow(var9);
            return null;
        }
    }

    private void fillArray(FSTObjectInput in, FSTClazzInfo serializationInfo, FSTClazzInfo.FSTFieldInfo referencee, int streamPosition, Object res, int len) throws ClassNotFoundException, IOException {
        in.registerObject(res, streamPosition, serializationInfo, referencee);
        Collection col = (Collection)res;
        if (col instanceof ArrayList) {
            ((ArrayList)col).ensureCapacity(len);
        }

        for(int i = 0; i < len; ++i) {
            Object o = in.readObject();
            col.add(o);
        }

    }
}
