package ru.vadlit.platform.web.exceptions

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.springframework.util.Assert
import org.springframework.util.StringUtils
import ru.vadlit.platform.web.exceptions.VndErrors.VndError
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlType

/**
 * A representation model class to be rendered as specified for the media type `application/vnd.error`.
 *
 * @author Oliver Gierke
 * @see "https://github.com/blongden/vnd.error"
 */
@XmlRootElement(name = "errors")
@JsonSerialize(using = VndErrorSerializer::class)
class VndErrors : Iterable<VndError> {

    @XmlElement(name = "error")
    private val vndErrors: MutableList<VndError>

    constructor(message: String?, path: String?) : this(VndError(message, path))

    constructor(error: VndError, vararg errors: VndError) {
        Assert.notNull(error, "Error must not be null")
        vndErrors = ArrayList(errors.size + 1)
        vndErrors.add(error)
        vndErrors.addAll(listOf(*errors))
    }

    @JsonCreator
    constructor(errors: MutableList<VndError>) {
        Assert.notNull(errors, "Errors must not be null!")
        Assert.isTrue(errors.isNotEmpty(), "Errors must not be empty!")
        vndErrors = errors
    }

    protected constructor() {
        vndErrors = ArrayList()
    }

    fun add(error: VndError): VndErrors {
        vndErrors.add(error)
        return this
    }

    /**
     * Dummy method to allow [JsonValue] to be configured.
     *
     * @return the vndErrors
     */
    @get:JsonValue
    private val errors: List<VndError>
        get() = vndErrors

    /*
     * (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    override fun iterator(): MutableIterator<VndError> {
        return vndErrors.iterator()
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    override fun toString(): String {
        return String.format(
            "VndErrors[%s]",
            StringUtils.collectionToCommaDelimitedString(vndErrors)
        )
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    override fun hashCode(): Int {
        return vndErrors.hashCode()
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is VndErrors) {
            return false
        }
        return vndErrors == other.vndErrors
    }

    /**
     * A single [VndError].
     *
     * @author Oliver Gierke
     */
    @XmlType
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class VndError {
        /**
         * Returns the message of the error.
         *
         * @return the message
         */
        @XmlElement
        @JsonProperty
        val message: String?

        /**
         * Returns the path of the error.
         *
         * @return the path
         */
        @XmlAttribute
        @JsonProperty
        val path: String?

        /**
         * Returns the logref of the error.
         *
         * @return the logref
         */
        @XmlAttribute
        @JsonProperty
        val logref: String?

        /**
         * Returns error details map
         * @return Map
         */
        @JsonProperty
        val details: Map<String, Any>?

        /**
         * Creates a new [VndError] with the given logref a a message.
         *
         * @param message must not be null or empty.
         */
        constructor(message: String?) {
            Assert.hasText(message, "Message must not be null or empty!")
            this.message = message
            path = null
            logref = null
            details = null
        }

        /**
         * For internal usage only
         */
        constructor(message: String?, details: Map<String, Any>?) {
            Assert.hasText(message, "Message must not be null or empty!")
            this.message = message
            path = null
            logref = null
            this.details = details
        }

        /**
         * Creates a new [VndError] with the given logref and a message.
         *
         * @param message must not be null or empty.
         * @param path    must not be null or empty.
         */
        constructor(message: String?, path: String?) {
            Assert.hasText(message, "Message must not be null or empty!")
            Assert.hasText(path, "Path must not be null or empty!")
            this.message = message
            this.path = path
            logref = null
            details = null
        }

        /**
         * For internal usage only
         */
        constructor(
            message: String?,
            path: String?,
            details: Map<String, Any>?
        ) {
            Assert.hasText(message, "Message must not be null or empty!")
            Assert.hasText(path, "Path must not be null or empty!")
            this.message = message
            this.path = path
            logref = null
            this.details = details
        }

        /**
         * Protected default constructor to allow JAXB marshalling.
         */
        protected constructor() {
            message = null
            path = null
            logref = null
            details = null
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.hateoas.RepresentationModel#toString()
         */
        override fun toString(): String {
            return String.format("VndError[message: %s, path: %s, logref: %s]", message, path, logref)
        }

        override fun hashCode(): Int {
            val prime = 31
            var result = super.hashCode()
            result = prime * result + (logref?.hashCode() ?: 0)
            result = prime * result + (message?.hashCode() ?: 0)
            result = prime * result + (path?.hashCode() ?: 0)
            return result
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }

            if (!super.equals(other)) {
                return false
            }

            if (javaClass != other.javaClass) {
                return false
            }

            val other = other as VndError

            return logref == other.logref &&
                message == other.message &&
                path == other.path
        }
    }
}