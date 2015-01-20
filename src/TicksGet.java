import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import quote.SKQuoteLib;

import com.sun.jna.win32.StdCallLibrary.StdCallCallback;

public class TicksGet implements StdCallCallback
{
	public static String msg = "TicksGet";

	public void callback(short sMarketNo, short sStockidx, int nPtr, int nTime, int nBid, int nAsk,
			int nClose, int nQty)
	{
		int Status;
		SKQuoteLib.Stock stock = new SKQuoteLib.Stock();
		Status = SKQuoteLib.INSTANCE.SKQuoteLib_GetStockByIndex(sMarketNo, sStockidx, stock);
		if (Status == 0)
		{
			//symbol,bid,bidvol,ask,askvol,price,high,low,vol,totalvol,yesterdayclose
			String ticker = new String(stock.m_caStockNo).trim();
			msg = ticker + ",";
			msg += PriceShow(nBid, stock) + ",";
			msg += 0 + ",";
			msg += PriceShow(nAsk, stock) + ",";
			msg += 0 + ",";
			msg += PriceShow(nClose, stock) + ",";
			msg += PriceShow(stock.m_nHigh, stock) + ",";
			msg += PriceShow(stock.m_nLow, stock) + ",";
			msg += nQty + ",";
			msg += stock.m_nTQty + ",";
			msg += PriceShow(stock.m_nRef, stock);

			if (skquote.showgui == 0)
				System.out.println(msg);
			skquote.queue_price.add(msg);
			//skquote.label_TX00.setText(msg);
			skquote.display.wake();
		}
	}
	
	private int PriceShow(int PriceCallback, SKQuoteLib.Stock stock)
	{
		int divisor = PriceDivisor(stock.m_sDecimal);
		float Quotient = (float)PriceCallback/divisor;
		return (int)Quotient;
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
			return (int)Math.pow(10.0, m_sDecimal);
		}
	}

}
