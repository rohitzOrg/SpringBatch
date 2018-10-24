package io.batch.SpringBatch.domain;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class InsurancFieldSetMapper implements FieldSetMapper<InsuranceModel> {

	@Override
	public InsuranceModel mapFieldSet(FieldSet fieldSet) throws BindException {
		
		return new InsuranceModel(/*fieldSet.readLong("id")
									,*/ 
									fieldSet.readString("policyId")
									,fieldSet.readString("county")
									,fieldSet.readString("construction"));
	}

}
