import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.jna.ptr.ShortByReference;

import quote.*;
import quote.FOnNotifyQuote;
import quote.FOnNotifyServerTime;
import skosquote.*;
import skosquote.FOnNotifyConnection;

public class skquote
{
	private static final String POSITION_JSON = "capital_futurebot.json";
	static String ca_id = "A123456789", ca_password = "mypassword";
	static String ca_account = "F02000", ca_stock_account = "123456";
	static String symbol = "MTX00";

	public static Queue<String> queue = new LinkedList<String>();

	public static void main(String[] args)
	{
		int ret;
		// TODO Auto-generated method stub
		JSONParser parser = new JSONParser();
		try
		{
			Object obj = parser.parse(new FileReader(POSITION_JSON));
			JSONObject jsonObject = (JSONObject) obj;

			ca_id = (String) jsonObject.get("ca_id");
			System.out.println("ca_id: " + ca_id);

			ca_password = (String) jsonObject.get("ca_password");
			System.out.println("ca_password: " + ca_password);

			ca_account = (String) jsonObject.get("ca_account");
			System.out.println("ca_account: " + ca_account);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setSize(1600, 600);
		shell.setText("FutureBot群益報價");
		shell.setLayout(new RowLayout());

		System.out.println("start FutureBot");
		/*
		 * final Button button = new Button(shell, SWT.PUSH); button.setText("接收報價"); final Button close = new Button(shell, SWT.PUSH); close.setText("結束連線");
		 */
		final Label label_TX00 = new Label(shell, SWT.LEFT);
		label_TX00.setText("Connecting...");
		label_TX00.setLocation(0, 100);

		ret = SKQuoteLib.INSTANCE.SKQuoteLib_Initialize(ca_id, ca_password);
		if (ret == 0)
		{
			System.out.println("init quote ok");
			// fnkld=new FOnNotifyKLineData();
			// fnmt = new FOnNotifyMarketTot(skquotelib,twse_ohlc);
			// fnq = new FOnNotifyQuote(skquotelib,twse_ohlc);
			// int kline = skquotelib.SKQuoteLib_AttachKLineDataCallBack(fnkld);
			ret += SKQuoteLib.INSTANCE
					.SKQuoteLib_AttchServerTimeCallBack(new quote.FOnNotifyServerTime());
			ret += SKQuoteLib.INSTANCE
					.SKQuoteLib_AttachConnectionCallBack(new quote.FOnNotifyConnection());
			// int tot = skquotelib.SKQuoteLib_AttachMarketTotCallBack(fnmt);

			// ret += SKQuoteLib.INSTANCE.SKQuoteLib_AttachQuoteCallBack(new quote.FOnNotifyQuote());
			ret += SKQuoteLib.INSTANCE.SKQuoteLib_AttachTicksGetCallBack(new TicksGet());
			SKQuoteLib.INSTANCE.SKQuoteLib_EnterMonitor();
			// SKQuoteLib.INSTANCE.SKQuoteLib_RequestStocks(sbr_one, "TX00,TSLD");
		}

		// os
		ret = SKOSQuoteLib.INSTANCE.SKOSQuoteLib_Initialize(ca_id, ca_password);
		if (ret == 0)
		{
			// fnkld=new FOnNotifyKLineData();
			// fnmt = new FOnNotifyMarketTot(skquotelib,twse_ohlc);
			// fnq = new FOnNotifyQuote(skquotelib,twse_ohlc);
			// int kline = skquotelib.SKQuoteLib_AttachKLineDataCallBack(fnkld);
			ret += SKOSQuoteLib.INSTANCE
					.SKOSQuoteLib_AttachServerTimeCallBack(new skosquote.FOnNotifyServerTime());
			ret += SKOSQuoteLib.INSTANCE
					.SKOSQuoteLib_AttachConnectCallBack(new skosquote.FOnNotifyConnection());
			// int tot = skquotelib.SKQuoteLib_AttachMarketTotCallBack(fnmt);
			// ret += SKOSQuoteLib.INSTANCE.SKOSQuoteLib_AttachQuoteCallBack(new skosquote.FOnNotifyQuote());
			ret += SKOSQuoteLib.INSTANCE
					.SKOSQuoteLib_AttachStockCallBack(new skosquote.FOnStockGet());
			ret += SKOSQuoteLib.INSTANCE.SKOSQuoteLib_AttachTicksGetCallBack(new TicksGetOS());

			SKOSQuoteLib.INSTANCE.SKOSQuoteLib_EnterMonitor((short) 0);
		}

		try
		{
			Thread.sleep(5000); // 1000 milliseconds is one second.
		}
		catch (InterruptedException ex)
		{
			Thread.currentThread().interrupt();
		}
		
		ShortByReference sbr_zero = new ShortByReference((short) 0);
		ShortByReference sbr_neg = new ShortByReference((short) -1);
		String tick = "TX00";
		SKQuoteLib.INSTANCE.SKQuoteLib_RequestTicks(sbr_neg, tick);
		
		shell.open();
		Point p;
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
				display.sleep();
			label_TX00.setText(TicksGet.msg);
			p = label_TX00.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			label_TX00.setBounds(0, 100, p.x+5, p.y+5);
		}
		display.dispose();

	}

}
