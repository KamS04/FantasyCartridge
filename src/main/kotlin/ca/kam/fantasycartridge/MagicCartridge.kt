package ca.kam.fantasycartridge

import ca.kam.vmhardwarelibraries.FantasyCartridge
import ca.kam.vmhardwarelibraries.cpu.CPUInterface
import ca.kam.vmhardwarelibraries.memory.MemoryDevice

@OptIn(ExperimentalUnsignedTypes::class)
class MagicCartridge: FantasyCartridge {
    private var mStart: UShort? = null

    override val isForkedGraphics: Boolean = false
    override val startAddress: UShort
        get() = mStart ?: throw Exception("Cartidge not loaded, start address unknown")

    override fun exitGraphics() {
        throw Exception("Is not forked graphics, exit graphics should not be called")
    }

    override fun initializeGraphics(cpu: CPUInterface) {
        // NO GRAPHICS TO INITIALIZE
    }

    override fun loadInto(memory: MemoryDevice, size: Int) {
        val romFile = object {}.javaClass.getResourceAsStream("/rom")  ?: throw Exception("Where ROM?????")
        val romBytes = romFile.readBytes().toUByteArray()
        if (size < romBytes.size)
            throw Exception("Woah there RAM is too little")

        memory.load(romBytes, 0u)

        val codeAddress = romBytes.size.toUShort()
        val codeFile = object {}.javaClass.getResourceAsStream("/code") ?: throw Exception("Where CODE?????")
        val codeBytes = codeFile.readBytes().toUByteArray()
        if (size < romBytes.size + codeBytes.size)
            throw Exception("Woah there RAM is too little")
        memory.load(codeBytes, romBytes.size.toUShort())

        val codeMeta = object {}.javaClass.getResourceAsStream("/cmeta")

        mStart = codeMeta?.let {
            val b1 = it.read()
            val b2 = it.read()
            return@let b1.shl(8).or(b2).toUShort()
        } ?: 0u
    }

    override fun startCPU(cpu: CPUInterface) {
        println("...preinitialized cpu is good...\n")
    }
}