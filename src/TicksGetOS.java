import com.sun.jna.win32.StdCallLibrary.StdCallCallback;

public class TicksGetOS implements StdCallCallback
{
	public static String msg = "TicksGetOS";

	public void callback(short sStockidx, int nPtr, int nTime, int nClose, int nQty)
	{

	}
}
