package am

import am.network.{MessagePacket, MessageTooLongException, NetworkActorAddress, UDPServer}
import org.scalatest.FunSuite

class UDPServerReceiveSuite extends FunSuite{

  val server1 = new UDPServer()

  val server2 = new UDPServer()

  val bufTooLong: Array[Int] = for(i <- (0 to 16*1024).toArray) yield i

  val naa = new NetworkActorAddress(server2.socketAddress, 1)

  val mp = MessagePacket(ActorAddress.noAddress, naa, bufTooLong)

  test("Sending too long message should throw an exception"){
    assertThrows[MessageTooLongException]{
      server1.send(mp)
    }
  }

  val anInt: Integer = 10

  val mp2 = MessagePacket(ActorAddress.noAddress, naa, anInt)

  server1.send(mp2)

  val receivedMp: MessagePacket = server2.receive()

  test("Sent MessagePacket and received MessagePacket should be the same"){
    assert(mp2.from == receivedMp.from)
    assert(mp2.to == receivedMp.to)
    assert(mp2.contents == receivedMp.contents)
  }

}
