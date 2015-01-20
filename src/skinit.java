import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import quote.SKQuoteLib;
import skosquote.SKOSQuoteLib;

public class skinit extends Thread
{
	public void run()
	{
		//String tName = Thread.currentThread().getName();
		int ret;

		// os
		ret = SKOSQuoteLib.INSTANCE.SKOSQuoteLib_Initialize(skquote.ca_id, skquote.ca_password);
		if (ret == 0)
		{
			ret += SKOSQuoteLib.INSTANCE
					.SKOSQuoteLib_AttachServerTimeCallBack(new skosquote.FOnNotifyServerTime());
			ret += SKOSQuoteLib.INSTANCE
					.SKOSQuoteLib_AttachConnectCallBack(new skosquote.FOnNotifyConnection());
			// int tot = skquotelib.SKQuoteLib_AttachMarketTotCallBack(fnmt);
			// ret += SKOSQuoteLib.INSTANCE.SKOSQuoteLib_AttachQuoteCallBack(new skosquote.FOnNotifyQuote());
			ret += SKOSQuoteLib.INSTANCE
					.SKOSQuoteLib_AttachStockCallBack(new skosquote.FOnStockGet());
			if (skquote.RequestTicks == 1)
				ret += SKOSQuoteLib.INSTANCE.SKOSQuoteLib_AttachTicksGetCallBack(new TicksGetOS());

			ret += SKOSQuoteLib.INSTANCE.SKOSQuoteLib_EnterMonitor((short) 0);
			System.out.println(new String("OS EnterMonitor = " + ret));
		}

		ret = SKQuoteLib.INSTANCE.SKQuoteLib_Initialize(skquote.ca_id, skquote.ca_password);
		if (ret == 0)
		{
			// System.out.println("SKQuoteLib_Initialize ok");
			ret += SKQuoteLib.INSTANCE
					.SKQuoteLib_AttchServerTimeCallBack(new quote.FOnNotifyServerTime());
			ret += SKQuoteLib.INSTANCE
					.SKQuoteLib_AttachConnectionCallBack(new quote.FOnNotifyConnection());
			// int tot = skquotelib.SKQuoteLib_AttachMarketTotCallBack(fnmt);

			ret += SKQuoteLib.INSTANCE
					.SKQuoteLib_AttachProductsReadyCallBack(new quote.FOnProductsReady());
			if (skquote.RequestTicks == 1)
				ret += SKQuoteLib.INSTANCE.SKQuoteLib_AttachTicksGetCallBack(new TicksGet());
			else
				ret += SKQuoteLib.INSTANCE
						.SKQuoteLib_AttachQuoteCallBack(new quote.FOnNotifyQuote());
			ret += SKQuoteLib.INSTANCE.SKQuoteLib_EnterMonitor();
			System.out.println(new String("EnterMonitor = " + ret));
		}
		
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(600, 600);
		shell.setText("skinit");
		shell.setLayout(new RowLayout());
		shell.open();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
