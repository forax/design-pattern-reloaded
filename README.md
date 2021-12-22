# Design Patterns Reloaded
Implementation of some design patterns in Java 17,
some of them are from the GoF, others are the one you usually see in programs.

Like most of the GoF patterns, the implementations use delegation instead of inheritance,
but also emphasis immutability when necessary.

- [abstract factory](src/main/java/abstractfactory) abstracts access to several factories
- [adapter](src/main/java/adapter) to see an object from one interface as one from another interface
- [builder](src/main/java/builder) constructs objects by name instead of by position
- [chain of responsibility](src/main/java/chainofresponsibility) constructs a pipeline of objects that handle a request
- [command](src/main/java/command), objects that are an action
- [curry](src/main/java/curry), delay and change the order of the arguments of a function
- [decorator](src/main/java/decorator) dynamically adds behavior to an existing object
- [factory](src/main/java/factory) abstracts the creation of objects
- [memoizer](src/main/java/memoizer) caches the result of a computation to avoid multiple re-computations of the same value
- [monad](src/main/java/monad) wraps multiple disjoint states under a common API 
- [observer](src/main/java/observer) de-couples codes by pushing the values from an object to another one
- proxy see [decorator](src/main/java/decorator)
- singleton see [abstract-factory](src/main/java/abstractfactory)
- [railwayswitch](src/main/java/railwayswitch) abstracts a cascade of if ... else
- [state](src/main/java/state) delegates API implementation to an internal state object
- [typing](src/main/java/typing), 3 kinds of relations between a static type and a runtime class
- [visitor](src/main/java/visitor), specify operations on a hierarchy of types outside that hierarchy


## Old materials

I've done a presentation in English at Devoxx 2015

Corresponding [slides](https://speakerdeck.com/forax/design-pattern-reloaded-parisjug) used for
[my presentation at ParisJUG in June 2015](http://www.parisjug.org/xwiki/wiki/oldversion/view/Meeting/20150602).

NB: A port exists in [Scala](https://github.com/YannMoisan/design-pattern-reloaded)
