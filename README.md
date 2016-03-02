# Meta

A meta model for Java and representations for other data formats.

Typical use-cases for programming at a meta level:
* Validate
* Convert
* Diff


## Principles

Data consists of scalars (atomic types), sequences (repeated values), and mappings (a sequence of key-value pairs).
Data can be represented in various formats, POJOs, collections, xml, json, or yaml documents, to name just a few.
All of these formats have some meta data, like whitespace, comments, type/schema information, distinction between attributes
and elements, read-only flags, and much more.
While all data formats should be able to represent all data, meta data is often specific to one such representation and invalid in others.

## TODOs

* Pretty-print JSON
* JSON guide
* XML guide
* XML generator

## Challenges

Things not yet implemented that can potentially kill the purpose of the whole project:
* Dynamic sub-types
* Meta-Data
* Schemas
* SchemaCompilers
* Streaming write (builder)
* Binding

