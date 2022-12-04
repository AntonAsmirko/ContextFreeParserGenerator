package exception

import java.lang.IllegalStateException

class EOFException(msg: String): IllegalStateException(msg)