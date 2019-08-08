package engine.scene.terrain.indexpool;

public class TIndexPoolData {
	
	public int vertexcnt, lodjmp;

	public TIndexPoolData(int vertexcnt, int lodjmp) {
		this.vertexcnt = vertexcnt;
		this.lodjmp = lodjmp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lodjmp;
		result = prime * result + vertexcnt;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TIndexPoolData other = (TIndexPoolData) obj;
		if (lodjmp != other.lodjmp)
			return false;
		if (vertexcnt != other.vertexcnt)
			return false;
		return true;
	}

}
