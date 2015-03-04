# Welcome to SimpleSSL

SimpleSSL is a simple way to work with OpenSSL compatible artifacts in Java. The project comes from frustration with trying to
perform common tasks that can be accomplished easily with OpenSSL. I have found that doing this from Java is painful and is
prone to a fair number of issues.

We are told again and again that cryptography is *hard* and that we must take care to get it right.  The current state of the
Java API for dealing with most cryptographic tasks is... complex.

SimpleSSL aims to change that by creating a clean, fluent, and descriptive API. If you want to get started keep reading. If you aren't 
persuaded then just [skip ahead to the section for skeptics](#skeptical).

### Wait, what?

This whole thing started when I realized that it was *easier* to use Java to call into the JRuby to use the OpenSSL bindings that had
been written for JRuby (jruby-openssl).  I replaced 480 lines of Java on a project with around 300 lines of combined Java/Ruby code.  That
*included* the code required for bridging between Java and JRuby.  (The actual Ruby code was only 119 lines.  The first real version of
this README file, at 380 lines, was shorter than the original Java code that started this mess!)

If you think that **any of this** means that I hate the guys over at [Bouncy Castle](https://www.bouncycastle.org/) well, [you're wrong!](#lovethecastle)

<br/>

---

# Hey, that's great.  What now?

You should read about what SimpleSSL can do and how you can use it.  It should be fairly easy to get started but we've decided to go ahead and lay out
some of the basics here.
 
## Features

* Support for RSA key types
    * PEM and DER encoding
        * PCKS#1
    * PEM encoded PCKS#8
* Support for x509 Certificates (v1 and v3)
    * SHA1 and SHA2 signature types
    
## Upcoming Features

* Support for reading and writing x509 certificates
* Support for creating and applying CSRs
* Support for PCKS#12
* Support for encryption when reading/writing
* Support for creating an SSL Context from RSA keys
* Support for validating Certificates and Certificate chains

## Prerequisites and Dependencies

This library only requires Maven to get started.  It will, however, download several supporting libraries:

* Bouncy Castle - 1.51
* Guava - 18.0
* SLF4J - 1.6.4

And for testing:

* JUnit 4.12
* Logback Classic 1.1.2
* Apache Commons-IO 2.4

## Building

To build, the simple Maven command ```mvn clean install``` will get you started.  This will add the artifacts to your local Maven repo.

## Maven

For maven you just need the dependency ```com.github.chrisruffalo:simplessl:1.0-SNAPSHOT```

``` xml
<dependency>
    <groupId>com.github.chrisruffalo</groupId>
    <artifactId>simple-ssl</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
<br/>

---

# Using SimpleSSL

This library is designed to have a *straightforward*, *simple*, and *fluent* API.  The primary consideration is *ease of use* and **never**
performance or features.  The (API)[#api] has several main entry points that are outlined in the next section.  These are [Keys](#keys) and [Certificates](#certificates).

## Quick Facts

Most of the objects in the API that have a direct analog in the JCE are type-compatible where possible.  These objects can be unwrapped through the ```unwrap()``` method
to convert to a JCE object if required.  Objects, like ```KeyPair```, that cannot extend interfaces and types from the JCE API also support ```unwrap()``` so that
they can be used with JCE types.

The SimpleSSL API also makes use of ```Optional<>``` and ```Either<>``` to support safe programming.  This allows the user to inspect many of the returned values without worrying
about NullPointer exceptions.  SimpleSSL makes it a policy to avoid returning nulls or throwing checked **or** unchecked exceptions where possible.

If something goes wrong SimpleSSL will return an empty Optional<> or an Either<> populated (right side) with errors.  Warnings and suggestions will be logged using SLF4J so that
the consumers of SLF4J can have an opportunity to see and fix their mistakes during testing.

The Optional<> implementation comes from Guava for supporting the 1.7 JDK and the Either<> implementation is part of the SimpleSSL API.

## Quick Examples <a name="examples"></a>

### **Generate a RSA Key**

``` java
KeyPair pair = Keys.generateRSA(2048);
```

One line that *matches* the way that the OpenSSL, Ruby, C, C++, C\#, Python, and Perl APIs work.  Seriously.

### **Read a RSA Key (PEM or DER PKCS\#1)**

``` java
Path privateKeyPath = Paths.get("/keys/private_key.pem");
Optional<RSAPrivateKey> privateRSAKeyOption = Keys.read(privateKeyPath);
```

Read, in just about any common format, a private key file.  If something goes wrong the key will be absent from the Option.

### **Write a Key to DER or PEM**

``` java
Path outputPemPath = Paths.get("/keys/private_key.pem");
Keys.writePEM(privateKey, outputPemPath);

Path outputDerPath = Paths.get("/keys/private_key.der");
Keys.writePEM(privateKey, outputDerPath);
```

That's it!  It uses Java new file API to handle the paths and it provides a simple interface for writing keys.

### **Read public key from private key (where supported)**

``` java
Path privateKeyPath = Paths.get("/keys/private_key.pem");
Optional<RSAPrivateKey> privateKeyOption = Keys.read(privateKeyPath);

RSAPrivateKey privateKey = privateKeyOption.get();
Optional<RSAPublicKey> publicKey = privateKey.publicKey();
```

This depends on the ability to derive/reconstruct the private key from what was given and may not be available with all types so it comes with an Optional<> so that
you can determine if the key was found without having to sort through a bunch of nasty exceptions.

## API Overview <a name="api"></a>

* [Keys](#keys)
* [Certificates](#certificates)

### Keys <a name="keys"></a>

The entry point for Keys is ```com.github.chrisruffalo.simplessl.Keys```.  This is used to read Keys, generate Keys, and otherwise 

### Certificates <a name="certificates"></a>

The entry point for Certificates is ```com.github.chrisruffalo.simplessl.Certificates```.


<br/>

---

# Further Reading

There's [a lot](http://i.imgur.com/KWt7Tkx.png) more to say about the motivation behind SimpleSSL.  I really feel like tools to make cryptography more
accessible to the layman (which I most certainly am) are necessary to improve the methodologies and practices of the average user.

## Ok, so you don't believe SimpleSSL is useful <a name="skeptical"></a>

That's fair, it really is, but hear me out... doing these things in Java *is* hard and, what makes it worse, different from every other API.
This matters because it makes it *harder* to puzzle out what to do next. 

Using the top Google results for "${language} rsa key, we can create an single RSA key in a few different ways:

### **Direct OpenSSL**

``` shell
openssl genrsa -out private_key.pem 2048
```

Well, that was easy. Let's try a few other languages.

### **Ruby**

``` ruby
require 'openssl'
private_key = OpenSSL::PKey::RSA.new 2048
```

Ok, that's great! How about more.

### **Python**

``` python
from Crypto.PublicKey import RSA
key = RSA.generate(2048)
```

Nice! Again, a simple way.

### **Perl**

``` perl
import Crypt::RSA::Key
my ($public, $private) = $keychain->generate (Size => 2048) or die $keychain->errstr();
```
That was a little odd. Still, I think it works ok.

### **C**

``` c
#include <openssl/rsa.h>
RSA *rsa = RSA_generate_key(2048, 65537, 0, 0);
```

Yikes! I would have expected C to be harder than that! What else?

### **C++**

``` c++
#include <openssl/rsa.h>
r = RSA_new();
ret = RSA_generate_key_ex(r, 2048, 65537, NULL);
```

Ok, so that just uses a slightly different API than the C example.  (The method the C version calls is deprecated and should just be used
pretty much the same way that this example is.)

### **C\#**

``` c#
using System.Security.Cryptography;
RSACryptoServiceProvider rsaProvider = new RSACryptoServiceProvider(2048);
```

Alright, I'll be honest and say that the C# abstraction is... really abstract. But it works.

### **PHP**

``` php
$config = array('private_key_bits' => 512);
$privKey = openssl_pkey_new($config);
```

Even PHP gets it simple.

### **Java**

``` java
KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
kpg.initialize(2048);
KeyPair kp = kpg.genKeyPair();
```

I'll admit that it doesn't look that bad here but it really is already a little more verbose than the others. Maybe this isn't a fair example but in most of the languages
that I surveyed it was a *one line* affair. I'm not normally one to bash on Java for verbosity but there is a limit. The *reason* the verbosity is bad is because
it is so out of line with other implementations. This is also **ignoring** any exceptions that might be thrown and the ways that we can work on removing, preventing,
and mitigating error conditions.  (Java usually involves going through another level of abstraction.)

I'll expand. Using the search "${language} rsa key to file" would be unfair, I feel, because Java gets a little verbose in that area but there are a lot of steps involving
things like PEM conversion and such. That's an exercise that would normally be left to the reader.

So, we can look at the following "${language} rsa key from file" which is where most of my original frustration comes from. We'll **assume** the key is in the PEM format
because, if we don't, Java has a really hard time. (I'll leave out the import stuff from here forward.)

Here are the examples:

### **Ruby**

``` ruby
private_key = OpenSSL::PKey::RSA.new File.read "/keys/private_key.pem"
```

Oh, wow.

### **C**

``` c
FILE *keyfile = fopen("/keys/private_key.pem", "r");
RSA *rsa_pri = PEM_read_RSAPrivateKey(keyfile, NULL, NULL, NULL);
```

Again, find file and open it. That's pretty much it.

### **Java**

``` java
Security.addProvider(new BouncyCastleProvider());

Reader reader = new BufferedReader(new FileReader("/keys/private_key.pem"));

PEMParser pp = new PEMParser(reader);
PEMKeyPair pemKeyPair = (PEMKeyPair) pp.readObject();
KeyPair kp = new JcaPEMKeyConverter().getKeyPair(pemKeyPair);
pp.close();
```

Ok, so in this case the best way is to go through the Bouncy Castle API.  This reduces the issues but it still involves going through a little bit of wading through
an abstraction.

So, that looks easy right?  But we've already deviated from the normal pattern: open file handle then read RSA key.  We need to parse the key, take the parsed
key pair, and then convert that to a ```java.security.KeyPair```.

What about extracting the public key from a given private key.  The information is there so, let's do it!  (Google string "${language} rsa public key from private key.")

### **Direct OpenSSL**

``` shell
openssl rsa -in /keys/private_key.pem -pubout > /keys/public_key.pem
```

### **Ruby**

``` ruby
private_key = OpenSSL::PKey::RSA.new File.read "/keys/private_key.pem"
public_key = private_key.public_key
```

### **C**

``` c
// todo
```

### **Java**

``` java
// todo
```

Let's look at one more example.  Convert from DER to PEM.  For these I've used the Google search string "${language} rsa der to pem" but I know this one
is rigged because the Java way is... not very straightforward.

### **Direct OpenSSL**

``` shell
openssl rsa -inform DER -outform PEM -in /keys/private_key.der -out /keys/private_key.pem
```

### **Ruby**

``` ruby
private_key = OpenSSL::PKey::RSA.new File.read "/keys/private_key.der"
open "/keys/private_key.der", 'w' do |io| io.write private_key.to_pem end
```

This is really what made me want to write another API.  This is so quick and easy!

### **C**

``` c
// todo
```

### **Java**

``` java
// todo
```

## Man, that's a lot of words

So, here, I'll bottom line it for you: the Java APIs are not tailored for dealing with this stuff.  The JCE is nice when you have *every algorithm in the world* at your disposal.
But most of us don't need that.  What most of us want is to be able to deal with SSL concepts in OpenSSL terms.

*None* of those Java examples deal with encrypted keys, serious error handling, file permissions, and a ton of other gotchas.  Most of the examples spend a lot of time going 
between JCE and the Bouncy Castle APIs.

How does SimpleSSL fix this?  By presenting a single API that is oriented around the types of SSL objects you will deal with.  Go back and [look at some of the examples](#examples) 
and how SimpleSSL deals with the tasks that can be painful without the simplified API.

## What about Bouncy Castle? <a name="lovethecastle"></a>

Bouncy Castle is amazing!  I could never, ever, ever begin to implement the things that they have.  But it is rather general, and that's nobody's fault, because
it is an implementation, and a really comprehensive one, of the [JCE](http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html).
  
What it is not is a specific compatibility layer or companion to OpenSSL or any other specific implementation.  We utilize their work heavily, trust it, and
couldn't do any of this without it.

## Conclusion

So, you can see that we've tried to make this as easy as possible.  In dealing with a reduced set of cryptography functions that aims to be compatible with OpenSSL we
have significantly reduced the number of edge cases and drastically reduced the complexity of the surface API.  We can make alot of solid assumptions about the
work that is being done and try and tailor the API directly to OpenSSL, RSA, PKI, and related tasks.

