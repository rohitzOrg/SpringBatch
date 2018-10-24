package io.batch.SpringBatch.domain;

public class InsuranceModel {
	
	//private final long id;
	private final String policyId;
	private final String county;
	private final String construction;
	
	public InsuranceModel(/*long id,*/String policyId, String county, String construction) {
		/*this.id=id;*/
		this.policyId = policyId;
		this.county = county;
		this.construction = construction;
	}

	public String getPolicyId() {
		return policyId;
	}

	public String getCounty() {
		return county;
	}

	public String getConstruction() {
		return construction;
	}

	/*public long getId() {
		return id;
	}*/

	@Override
	public String toString() {
		return "InsuranceModel [ policyId=" + policyId + ", county=" + county + ", construction="
				+ construction + "]";
		//id=" + id + ",
	}


}
