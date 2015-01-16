import skosquote.SKOSQuoteLib;

import com.sun.jna.win32.StdCallLibrary.StdCallCallback;

public class TicksGetOS implements StdCallCallback
{
	public static String msg = "TicksGetOS";

	public void callback(short sStockidx, int nPtr, int nTime, int nClose, int nQty)
	{
		int Status;
		SKOSQuoteLib.FOREIGN foreign = new SKOSQuoteLib.FOREIGN();
		Status = SKOSQuoteLib.INSTANCE.SKOSQuoteLib_GetStockByIndex(sStockidx, foreign);
		if (Status == 0)
		{
			// symbol,bid,bidvol,ask,askvol,price,high,low,vol,totalvol,yesterdayclose
			String ticker = new String(foreign.m_caStockNo).trim();
			msg = ticker + ",";
			msg += 0 + ",";
			msg += 0 + ",";
			msg += 0 + ",";
			msg += 0 + ",";
			msg += PriceShow(nClose, foreign) + ",";
			msg += PriceShow(foreign.m_nHigh, foreign) + ",";
			msg += PriceShow(foreign.m_nLow, foreign) + ",";
			msg += nQty + ",";
			msg += foreign.m_nTQty + ",";
			msg += PriceShow(foreign.m_nRef, foreign);// 群益海期API都是0

			//System.out.println(msg);
			//skquote.queue_price.add(msg);
		}
	}

	private float PriceShow(int PriceCallback, SKOSQuoteLib.FOREIGN foreign)
	{
		int divisor = PriceDivisor(foreign.m_sDecimal);
		float Quotient = (float) PriceCallback / divisor;

		int before_decimal = PriceCallback / divisor;
		float after_decimal = Quotient - before_decimal;
		after_decimal *= foreign.m_nDenominator;
		after_decimal += before_decimal;
		return after_decimal;
	}

	private int PriceDivisor(short m_sDecimal)
	{
		switch (m_sDecimal)
		{
		case 2:
			return 100;
		case 3:
			return 1000;
		case 4:
			return 10000;
		case 5:
			return 100000;
		case 6:
			return 1000000;
		default:
			return (int) Math.pow(10.0, m_sDecimal);
		}
	}
}
