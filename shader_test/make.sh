#!/bin/sh

mkdir -p net/walker9/fractaly/shader/
cp ../client/app/src/main/java/net/walker9/fractaly/shader/* net/walker9/fractaly/shader/

javac Tester.java

java Tester 0 &
java Tester 1 &
java Tester 2 &
java Tester 3 &
wait

rm -r net/
