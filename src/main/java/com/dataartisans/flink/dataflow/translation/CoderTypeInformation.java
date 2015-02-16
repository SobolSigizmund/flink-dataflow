package com.dataartisans.flink.dataflow.translation;

import com.google.api.client.repackaged.com.google.common.base.Preconditions;
import com.google.cloud.dataflow.sdk.coders.Coder;
import com.google.cloud.dataflow.sdk.coders.KvCoder;
import com.google.cloud.dataflow.sdk.coders.VoidCoder;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeutils.CompositeType;
import org.apache.flink.api.common.typeutils.TypeComparator;
import org.apache.flink.api.common.typeutils.TypeSerializer;

import java.util.List;

public class CoderTypeInformation<T> extends CompositeType {

	private Coder<T> coder;
	
	public CoderTypeInformation(Coder<T> coder) {
		super(null);
		this.coder = coder;
		Preconditions.checkNotNull(coder);
	}

	public TypeComparator<T> createComparator(int[] logicalKeyFields, boolean[] orders, int logicalFieldOffset) {
		return new KvCoderComperator((KvCoder) coder);
	}

	@Override
	public boolean isBasicType() {
		return false;
	}

	@Override
	public boolean isTupleType() {
		return false;
	}

	@Override
	public int getArity() {
		return 0;
	}

	@Override
	public Class<T> getTypeClass() {
		return (Class<T>) Object.class;
	}

	@Override
	public boolean isKeyType() {
		return true;
	}

	@Override
	public TypeSerializer<T> createSerializer() {
		if (coder instanceof VoidCoder) {
			return (TypeSerializer<T>) new CoderVoidTypeSerializer();
		}
		return new CoderTypeSerializer<>(coder);
	}

	@Override
	public int getTotalFields() {
		return 0;
	}

	@Override
	public void getKey(String fieldExpression, int offset, List result) {
		result.add(new FlatFieldDescriptor(0, BasicTypeInfo.INT_TYPE_INFO));
	}

	@Override
	public TypeInformation<?> getTypeAt(int pos) {
		return null;
	}

	@Override
	protected void initializeNewComparator(int localKeyCount) {

	}

	@Override
	protected TypeComparator getNewComparator() {
		return null;
	}

	@Override
	protected void addCompareField(int fieldId, TypeComparator comparator) {

	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CoderTypeInformation that = (CoderTypeInformation) o;

		if (!coder.equals(that.coder)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return coder.hashCode();
	}

	@Override
	public String toString() {
		return "CoderTypeInformation{" +
				"coder=" + coder +
				'}';
	}
}
