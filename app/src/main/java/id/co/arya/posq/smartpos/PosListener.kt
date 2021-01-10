package id.co.arya.posq.smartpos

import com.whty.smartpos.service.Balance
import com.whty.smartpos.service.EMVCardLog
import com.whty.smartpos.service.EMVTermConfig
import com.whty.smartpos.tysmartposapi.ISmartPosListener
import com.whty.smartpos.tysmartposapi.OperationResult
import com.whty.smartpos.tysmartposapi.cardreader.CardInfo
import com.whty.smartpos.tysmartposapi.pinpad.PinResult
import com.whty.smartpos.tysmartposapi.pos.DeviceInfo
import com.whty.smartpos.tysmartposapi.pos.DeviceVersion
import com.whty.smartpos.tysmartposapi.scanner.ScanResult

class PosListener : ISmartPosListener {
    override fun onUpdateCAPK(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onGetStatus(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onKeyDown(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onSetSystemClock(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onGetDeviceSN(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onSetTermConfig(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onVerifyKeyB(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onReadMifareUltralightCard(p0: OperationResult?) {
        TODO("Not yet implemented")
    }

    override fun onSetAPN(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onSetLed(p0: Int, p1: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onGetTlvValue(p0: ByteArray?) {
        TODO("Not yet implemented")
    }

    override fun onInitPrinter(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onGetDeviceInfo(p0: DeviceInfo?) {
        TODO("Not yet implemented")
    }

    override fun onInstallApp(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onGetRandom(p0: OperationResult?) {
        TODO("Not yet implemented")
    }

    override fun onSetTlv(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onQueryECBalance(p0: Balance?) {
        TODO("Not yet implemented")
    }

    override fun onClearCAPK(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onGetTlvList(p0: ByteArray?) {
        TODO("Not yet implemented")
    }

    override fun onIncreaseValue(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onUpdateAID(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onWriteCustomData(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onDukptUpdateKSN(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onUpdateMainKey(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onUpdateMerchantAndTerminalNo(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onGetDeviceVersion(p0: DeviceVersion?) {
        TODO("Not yet implemented")
    }

    override fun onEncryptData(p0: OperationResult?) {
        TODO("Not yet implemented")
    }

    override fun onWriteBlock(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onDukptUpdateIPEK(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onSetPrintParameters(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onUpdateBatchAndFlowNo(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onSetYSpace(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onClosePSAMCard(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onActive(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onTransmitRF(p0: OperationResult?) {
        TODO("Not yet implemented")
    }

    override fun onSelectKeyGroup(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onCancelReadCard(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onIsCardExists(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onWriteValue(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onGetDevicePN(p0: OperationResult?) {
        TODO("Not yet implemented")
    }

    override fun onReadCustomData(p0: OperationResult?) {
        TODO("Not yet implemented")
    }

    override fun onGetTlvValueEncrypted(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onDisableStatusBar(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onReadBlock(p0: OperationResult?) {
        TODO("Not yet implemented")
    }

    override fun onReadCardType(p0: CardInfo?) {
        TODO("Not yet implemented")
    }

    override fun onStopSearchCard(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onUpdateTransKey(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onGetDeviceKSN(p0: OperationResult?) {
        TODO("Not yet implemented")
    }

    override fun onUpdateWorkKey(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onGetTlvListEncrypted(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onReadCard(p0: CardInfo?) {
        TODO("Not yet implemented")
    }

    override fun onWriteMifareUltralightCard(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onGetPinResult(p0: PinResult?) {
        TODO("Not yet implemented")
    }

    override fun onUninstallApp(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onTransceive(p0: OperationResult?) {
        TODO("Not yet implemented")
    }

    override fun onScanResult(p0: ScanResult?) {
        TODO("Not yet implemented")
    }

    override fun onOpenPSAMCard(p0: OperationResult?) {
        TODO("Not yet implemented")
    }

    override fun onGetEmvCardLog(p0: MutableList<EMVCardLog>?) {
        TODO("Not yet implemented")
    }

    override fun onGetTlvEncrypted(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onGetMagCardData(p0: CardInfo?) {
        TODO("Not yet implemented")
    }

    override fun onTransmitIC(p0: OperationResult?) {
        TODO("Not yet implemented")
    }

    override fun onVerifyKeyA(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onEncryptKSN(p0: OperationResult?) {
        TODO("Not yet implemented")
    }

    override fun onStartSearchCard(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onCalculateMac(p0: OperationResult?) {
        TODO("Not yet implemented")
    }

    override fun onHalt(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onTransmitPSAM(p0: OperationResult?) {
        TODO("Not yet implemented")
    }

    override fun onClearAID(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onGetTlv(p0: ByteArray?) {
        TODO("Not yet implemented")
    }

    override fun onTradeResponse(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onReadValue(p0: ByteArray?) {
        TODO("Not yet implemented")
    }

    override fun onGetAID(p0: MutableList<ByteArray>?) {
        TODO("Not yet implemented")
    }

    override fun onGetCAPK(p0: MutableList<ByteArray>?) {
        TODO("Not yet implemented")
    }

    override fun onGetTermConfig(p0: EMVTermConfig?) {
        TODO("Not yet implemented")
    }

    override fun onGetMerchantAndTerminalNo(p0: Array<out String>?) {
        TODO("Not yet implemented")
    }

    override fun onGetBatchAndFlowNo(p0: Array<out String>?) {
        TODO("Not yet implemented")
    }

    override fun onPowerOn(p0: OperationResult?) {
        TODO("Not yet implemented")
    }

    override fun onPowerOff(p0: Int) {
        TODO("Not yet implemented")
    }
}