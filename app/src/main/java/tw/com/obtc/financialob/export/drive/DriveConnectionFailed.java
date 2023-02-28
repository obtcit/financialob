package tw.com.obtc.financialob.export.drive;

//import com.google.android.gms.common.ConnectionResult;

public class DriveConnectionFailed {

//    public final ConnectionResult connectionResult;
    public final GoogleDriveClientV3.ConnectionResult connectionResult;

    /*public DriveConnectionFailed(ConnectionResult connectionResult) {
        this.connectionResult = connectionResult;
    }*/

    public DriveConnectionFailed(GoogleDriveClientV3.ConnectionResult connectionResult) {
        this.connectionResult = connectionResult;
    }

}
