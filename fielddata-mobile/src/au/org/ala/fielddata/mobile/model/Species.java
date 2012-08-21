package au.org.ala.fielddata.mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Species extends Persistent implements Parcelable {

	public String scientificName;
	public String commonName;

	public int profileImagePath;

	public Species(String scientificName, String commonName, int fileName) {
		this.scientificName = scientificName;
		this.commonName = commonName;
		this.profileImagePath = fileName;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		int tmpId = -1;
		if (id != null) {
			tmpId  = id;
		}
		dest.writeInt(tmpId);
		dest.writeInt(server_id);
		dest.writeString(scientificName);
		dest.writeString(commonName);
	}

	public static final Parcelable.Creator<Species> CREATOR = new Parcelable.Creator<Species>() {
		public Species createFromParcel(Parcel in) {
			return new Species(in);
		}

		public Species[] newArray(int size) {
			return new Species[size];
		}
	};
	
	private Species(Parcel in) {
		id = in.readInt();
		server_id = in.readInt();
		scientificName = in.readString();
		commonName = in.readString();
	}

}