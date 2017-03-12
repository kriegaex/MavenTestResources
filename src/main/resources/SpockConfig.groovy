import spock.lang.Specification

/**
 * Use like this in order to print Spock/Geb labels:
 *   given:_ "foo"
 *   when:_ "bar"
 *   then:_ "zot"
 */
class LabelPrinter {
  def _(def message) {
    println message
    true
  }
}

Specification.mixin LabelPrinter
