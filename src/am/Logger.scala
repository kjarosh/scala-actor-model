package am

class Logger(val name: String) {
  def apply(message: String) = info(message)

  def debug(message: String) = log("DEBUG", message)
  def info(message: String) = log("INFO", message)
  def err(message: String) = log("ERROR", message)
  def trace(message: String) = log("TRACE", message)
  def fatal(message: String) = log("FATAL", message)

  def log(tag: String, message: String) = {
    System.err.println("[" + tag + "] " + message);
  }
}
