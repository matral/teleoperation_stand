package Maestro;
import java.nio.ByteBuffer;

import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

public class PololuConnector {

	
	private static boolean deviceMatchesVendorProduct(Device device,
			short idVendor, short idProduct) {
		DeviceDescriptor descriptor = new DeviceDescriptor();
		int result = LibUsb.getDeviceDescriptor(device, descriptor);
		if (result != LibUsb.SUCCESS)
			throw new LibUsbException("Unable to read device descriptor",
					result);
		return idVendor == descriptor.idVendor()
				&& idProduct == descriptor.idProduct();
	}

	public static void setTarget(int position, int servo) {
		short vendorId = 0x1ffb;
		short productIDArray[] = { 0x0089, 0x008a, 0x008b, 0x008c };
		Context context = new Context();
		int result = LibUsb.init(context);
		if (result != LibUsb.SUCCESS)
			throw new LibUsbException("Unable to initialize libusb.", result);

		DeviceList list = new DeviceList();
		// result = LibUsb.getDeviceList(null, list);
		result = LibUsb.getDeviceList(context, list);
		if (result < 0)
			throw new LibUsbException("Unable to get device list", result);

		for (int i = 0; i < result; i++) {
			Device device = list.get(i);
			for (int Id = 0; Id < 1; Id++) {
				if (deviceMatchesVendorProduct(device, vendorId,
						productIDArray[Id])) {
					DeviceHandle handle = new DeviceHandle();
					int resultHandle = LibUsb.open(device, handle);
					if (resultHandle != LibUsb.SUCCESS)
						throw new LibUsbException("Unable to open USB device",
								resultHandle);
					try {
						ByteBuffer buffer = ByteBuffer.allocateDirect(1);
						//System.out.println("position " + position );
						buffer.put(new byte[] { 0 });
						int transfered = LibUsb.controlTransfer(handle,
								(byte) 0x40, (byte) 0x85,
								(short) (position * 4), (short) servo, buffer,
								(long) 5000);

					} finally {
						LibUsb.close(handle);
					}
				}
			}
		}

		
		LibUsb.exit(context);
	}
	public static void setAcceleration(int acceleration, int servo) {
		short vendorId = 0x1ffb;
		short productIDArray[] = { 0x0089, 0x008a, 0x008b, 0x008c };
		Context context = new Context();
		int result = LibUsb.init(context);
		if (result != LibUsb.SUCCESS)
			throw new LibUsbException("Unable to initialize libusb.", result);

		DeviceList list = new DeviceList();
		// result = LibUsb.getDeviceList(null, list);
		result = LibUsb.getDeviceList(context, list);
		if (result < 0)
			throw new LibUsbException("Unable to get device list", result);

		for (int i = 0; i < result; i++) {
			Device device = list.get(i);
			for (int Id = 0; Id < 1; Id++) {
				if (deviceMatchesVendorProduct(device, vendorId,
						productIDArray[Id])) {
					DeviceHandle handle = new DeviceHandle();
					int resultHandle = LibUsb.open(device, handle);
					if (resultHandle != LibUsb.SUCCESS)
						throw new LibUsbException("Unable to open USB device",
								resultHandle);
					try {
						ByteBuffer buffer = ByteBuffer.allocateDirect(1);
						//System.out.println("position " + position );
						buffer.put(new byte[] { 0 });
						int transfered = LibUsb.controlTransfer(handle,
								(byte) 0x40, (byte) 0x84,
								(short) (acceleration * 4), (short) (servo| 0x80), buffer,
								(long) 5000);

					} finally {
						LibUsb.close(handle);
					}
				}
			}
		}

		
		LibUsb.exit(context);
	}
	public static void setSpeed(int speed, int servo) {
		short vendorId = 0x1ffb;
		short productIDArray[] = { 0x0089, 0x008a, 0x008b, 0x008c };
		Context context = new Context();
		int result = LibUsb.init(context);
		if (result != LibUsb.SUCCESS)
			throw new LibUsbException("Unable to initialize libusb.", result);

		DeviceList list = new DeviceList();
		// result = LibUsb.getDeviceList(null, list);
		result = LibUsb.getDeviceList(context, list);
		if (result < 0)
			throw new LibUsbException("Unable to get device list", result);

		for (int i = 0; i < result; i++) {
			Device device = list.get(i);
			for (int Id = 0; Id < 1; Id++) {
				if (deviceMatchesVendorProduct(device, vendorId,
						productIDArray[Id])) {
					DeviceHandle handle = new DeviceHandle();
					int resultHandle = LibUsb.open(device, handle);
					if (resultHandle != LibUsb.SUCCESS)
						throw new LibUsbException("Unable to open USB device",
								resultHandle);
					try {
						ByteBuffer buffer = ByteBuffer.allocateDirect(1);
						//System.out.println("position " + position );
						buffer.put(new byte[] { 0 });
						int transfered = LibUsb.controlTransfer(handle,
								(byte) 0x40, (byte) 0x84,
								(short) speed , (short) servo, buffer,
								(long) 5000);

					} finally {
						LibUsb.close(handle);
					}
				}
			}
		}

		
		LibUsb.exit(context);
	}

}
