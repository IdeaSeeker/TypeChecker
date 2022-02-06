package types

case class TypeArrow(left: Type, right: Type) extends Type {

    override def toString = s"${left.toStringWithBrackets} => $right"

    override def toStringVerbose =
        s"TypeArrow(${left.toStringVerbose}, ${right.toStringVerbose}"

    override def applyTo(other: Type): Option[Type] =
        if (other == left)
            Some(right)
        else
            None
}

object TypeArrow {

    def fromString(input: String): Option[TypeArrow] = {
        val updatedInput = util.trimBrackets(input)
        if (!input.contains("=>") || !util.isBalanced(updatedInput))
            return None

        util.computeBalances(updatedInput).zipWithIndex.find { case (balance, index) =>
            balance == 0 && updatedInput.slice(index, index + 2) == "=>"
        }.flatMap { case (_, index) =>
            // trying to parse both parts of the type arrow
            val (left, right) = splitByIndex(updatedInput, index)
            for {
                leftType <- Type.fromString(left)
                rightType <- Type.fromString(right)
            } yield TypeArrow(leftType, rightType)
        }
    }

    private def splitByIndex(input: String, index: Int): (String, String) =
        (input.take(index), input.takeRight(input.length - index - 2))
}
