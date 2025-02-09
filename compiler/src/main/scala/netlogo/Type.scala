package netlogo

import utils.Context

object Type{
    def toString(typ: Type):String = {
        typ match {
            case Types.BoolType => "Boolean"
            case Types.FloatType => "Double"
            case Types.IntType => "Int"
            case Types.ListType(inner) => f"List[${toString(inner)}]"
            case Types.StringType => "String"
            case Types.UnitType => "Unit"
            case Types.AnyType => "Any"
            case Types.NoType => "Any"
            case Types.CodeGenType(value) => value
            case null => "Int"
        }
    }
    def defaultValue(typ: Type):String = {
        typ match {
            case Types.BoolType => "false"
            case Types.FloatType => "0"
            case Types.IntType => "0"
            case Types.ListType(inner) => f"Nil"
            case Types.StringType => "null"
            case Types.NoType => "0"
            case null => "0"
        }
    }
    def fromString(string: String)(implicit context: Context): Type = {
        string.toLowerCase match {
            case "any" => Types.AnyType
            case "int" => Types.IntType
            case "float" => Types.FloatType
            case "string" => Types.StringType
            case "boolean" => Types.BoolType
            case "unit" => Types.UnitType
            case other => {
                if (other.startsWith("list[")){
                    Types.ListType(fromString(other.substring(5, other.size-1)))
                }
                else if (other.startsWith("breedset[")){
                    Types.BreedSetType(context.getBreedPlural(other.substring(9, other.size-1)))
                }
                else if (other.startsWith("breed[")){
                    Types.BreedType(context.getBreedSingular(other.substring(6, other.size-1)))
                }
                else{
                    ???
                }
            }
        }
    }
}
abstract class Type(_parent: Type){
    val parent = _parent

    def hasAsParent(other: Type): Boolean = {
        if (other == Types.NoType){
            true
        }
        else if (other == this){
            true
        }
        else if (parent != Types.NoType){
            parent.hasAsParent(other)
        }
        else{
            false
        }
    }

    def isParentOf(other: Type): Boolean = {
        if (other == Types.NoType){
            true
        }
        else if (other == this){
            true
        }
        else if (other.parent != Types.NoType){
            isParentOf(other.parent)
        }
        else{
            false
        }
    }

    override def toString(): String = {
        Type.toString(this)
    }
}

object Types{
    case object IntType extends Type(FloatType)
    case object FloatType extends Type(AnyType)
    case object BoolType extends Type(AnyType)
    case object StringType extends Type(AnyType)
    case class BreedType(breed: Breed) extends Type(AnyType)
    case class BreedSetType(breed: Breed) extends Type(AnyType)
    case class ListType(inner: Type) extends Type(AnyType)
    case object AnyType extends Type(UnitType)
    case object NoType extends Type(null)
    case object UnitType extends Type(NoType)
    case class CodeGenType(value: String) extends Type(null)
}

trait Typed{
    private var typ: Type = Types.NoType
    private var defaultTyp: Type = Types.NoType
    var typeFixed: Boolean = false

    def setDefaultType(typ: Type) = {
        this.defaultTyp = typ
    }
    def setType(typ: Type, fixed: Boolean = false) = {
        this.typ = typ
        typeFixed = fixed
    }
    def getType():Type = {
        if (typ == Types.NoType){
            defaultTyp
        }
        else{
            typ
        }
    }

    /**
     * Change for the type for ntyp. Return true if different from current one.
     * 
     */ 
    def changeTypeFor(ntyp: Type):Boolean = {
        val ret = typ != ntyp
        typ = ntyp
        ret
    }

    /**
     * Restraint the object to the type
     * 
     * @return true if type set changed
     */
    def putIn(ntyp: Type):Boolean={
        ntyp match{
            case Types.ListType(inner) => {
                changeTypeFor(ntyp)
            }
            case _ =>{
                if (ntyp == Types.NoType){
                    false
                }
                else if (typ == Types.NoType){
                    changeTypeFor(ntyp)
                }
                else if (typ.isParentOf(ntyp)){
                    false
                }
                else if (!typeFixed && ntyp.isParentOf(this.typ)){
                    changeTypeFor(ntyp)
                }
                else{
                    throw new IllegalStateException("Object Type Cannot be change.")
                }
            }
        }
    }

    /**
     * Restraint the object to the type
     * 
     * @return true if type changed
     */
    def putIn(other: Typed):Boolean={
        putIn(other.typ)
    }

    /**
     * Return if the object can be specialized for the type
     */ 
    def canPutIn(ntyp: Type): Boolean = {
        ntyp match{
            case Types.ListType(inner) =>{
                true
            }
            case _ =>{
                if (typ == Types.NoType){
                    true
                }
                else if (typ.isParentOf(ntyp)){
                    true
                }
                else if (!typeFixed && ntyp.isParentOf(this.typ)){
                    true
                }
                else{
                    false
                }
            }
        }
    }

    /**
     * Return if the object can be specialized for the type
     */ 
    def canPutIn(other: Typed): Boolean = {
        canPutIn(other.typ)
    }
}