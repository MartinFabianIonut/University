#include <iostream>
#include <algorithm>
#include <vector>
#include <string>
using namespace std;
class A {
public:
	virtual void f() = 0;
};
class B :public A {
public:
	void f() override {
		cout << "f din B";
	}
};
class C :public B {
public:
	void f() override {
		cout << "f din C";
	}
};
int main() {
	vector<A*> v;
	A *b = new B();
	v.push_back(b);
	A* c = new C();
	v.push_back(c);
	for (auto e : v) { e->f(); }
	return 0;
}