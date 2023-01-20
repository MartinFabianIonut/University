#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <crtdbg.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
using std::string;
using std::vector;
using std::cout;
using std::endl;

namespace Test1
{
	class Smoothy
	{
	private:
		int pret;
	public:
		Smoothy() { pret = 0; }
		Smoothy(int _pret) : pret{ _pret } {}
		virtual ~Smoothy() = default;
		virtual int getPret() {
			return pret;
		}
		virtual string descriere() = 0;
	};

	class BasicSmoothy : public Smoothy
	{
	private:
		string nume;
	public:
		BasicSmoothy(int _pret, string _nume) : nume{ _nume }, Smoothy{ _pret } {}
		string descriere() override
		{
			return nume;
		}
	};

	class DecoratorSmoothy : public Smoothy
	{
	private:
		Smoothy* smooty;
	public:
		DecoratorSmoothy(Smoothy* _smooty) : smooty{ _smooty }
		{
		}
		~DecoratorSmoothy(){
			delete smooty;
		}
		int getPret() override{
			return smooty->getPret();
		}
		string descriere() override{
			return smooty->descriere();
		}
	};

	class SmoothyCuFrisca : public DecoratorSmoothy	{
	public:
		SmoothyCuFrisca(Smoothy* _smooty) : DecoratorSmoothy{ _smooty } {}
		int getPret() override{
			return DecoratorSmoothy::getPret() + 2;
		}
		string descriere() override{
			return DecoratorSmoothy::descriere() + " cu frisca";
		}
	};

	class SmoothyCuUmbreluta : public DecoratorSmoothy
	{
	public:
		SmoothyCuUmbreluta(Smoothy* _smooty) : DecoratorSmoothy{ _smooty } {}
		int getPret() override
		{
			return DecoratorSmoothy::getPret() + 3;
		}
		string descriere()
		{
			return DecoratorSmoothy::descriere() + " cu umbreluta";
		}
	};

	vector < Smoothy* > FSmoothy()
	{
		vector < Smoothy* > lista;

		Smoothy* kivi = new BasicSmoothy{ 5, "Kivi" };
		Smoothy* kivi2 = new BasicSmoothy{ 5, "Kivi" };
		Smoothy* Capsuni = new BasicSmoothy{ 10, "Capsuni" };
		kivi = new SmoothyCuFrisca(kivi);
		kivi = new SmoothyCuUmbreluta(kivi);
		Capsuni = new SmoothyCuFrisca(Capsuni);

		lista.push_back(kivi);
		lista.push_back(Capsuni);
		lista.push_back(kivi2);

		return lista;
	}

	void SmoothyMain()
	{
		auto lista = FSmoothy();

		std::sort(lista.begin(), lista.end(), [](Smoothy* S1, Smoothy* S2) {return S1->descriere() < S2->descriere(); });

		for (const auto& smooty : lista)
		{
			std::cout << smooty->descriere() << " " << smooty->getPret() << "\n";
		}

		for (auto& smooty : lista)
		{
			delete smooty;
		}
	}

	template < typename TN >
	class Geanta
	{
	private:
		TN nume;
		vector < TN > Tip;
		typename vector<TN>::iterator it;
	public:
		Geanta(){}
		Geanta(TN tip) : nume{ tip } { }

		Geanta operator+(TN aux)
		{
			Geanta ganta = *this;
			ganta.Tip.push_back(aux);
			return ganta;
		}
		Geanta& operator=(const Geanta& Ganta)
		{
			Tip = Ganta.Tip;
			return *this;
		}

		typename vector<TN>::iterator begin() {
			it = Tip.begin();
			return it;
		}
		typename vector<TN>::iterator end() {
			it = Tip.end();
			return it;
		}
	};

	void calatorie() {
		Geanta<string> ganta{ "Ion" };//creaza geanta pentru ion
		ganta = ganta + string{ "haine" }; //adauga obiect in ganta
		ganta + string{ "pahar" };
		for (auto o : ganta)
		{
			std::cout << o << "\n";
		}
	}
}

namespace Test2
{
	class Meniu
	{
	private:
		int pret;
	public:
		Meniu() {
			pret = 0;
		}
		Meniu(int _pret) : pret{ _pret } {}
		virtual ~Meniu() = default;
		virtual string descriere() = 0;
		virtual int getPret()
		{
			return pret;
		}
	};

	class MicDejun : public Meniu
	{
	private:
		string denumire;
	public:
		MicDejun(string _denumire) : denumire{ _denumire } {}
		int getPret() override
		{
			if (denumire == "Ochiuri")
				return 10;
			return 15;
		}
		string descriere() override
		{
			return denumire;
		}
	};

	class CuRacoritoare : public Meniu
	{
	private:
		Meniu* menu;
	public:
		CuRacoritoare(Meniu* _menu) : menu{ _menu } {}
		~CuRacoritoare() { delete menu; }
		int getPret() override
		{
			return menu->getPret() + 4;
		}
		string descriere() override
		{
			return menu->descriere() + " cu racoritoare";
		}
	};

	class CuCafea : public Meniu
	{
	private:
		Meniu* menu;
	public:
		CuCafea(Meniu* _menu) : menu{ _menu } {}
		~CuCafea() { delete menu; }
		int getPret() override
		{
			return menu->getPret() + 5;
		}
		string descriere() override
		{
			return menu->descriere() + " cu cafea";
		}
	};

	vector < Meniu* > f()
	{
		vector < Meniu* > lista;

		Meniu* menu = new MicDejun("Omleta");
		Meniu* menu2 = new MicDejun("Omleta");
		Meniu* menu3 = new MicDejun("Ochiuri");
		menu = new CuRacoritoare(menu);
		menu = new CuCafea(menu);
		menu3 = new CuCafea(menu3);

		lista.push_back(menu);
		lista.push_back(menu3);
		lista.push_back(menu2);

		return lista;
	}

	void meniuMain()
	{
		vector < Meniu* > lista = f();

		std::sort(lista.begin(), lista.end(), [](Meniu* m1, Meniu* m2) {return m1->getPret() > m2->getPret(); });

		for (const auto& menu : lista)
		{
			std::cout << menu->getPret() << " " << menu->descriere() << "\n";
		}

		for (auto& menu : lista)
		{
			delete menu;
		}
	}

	template < class TN>
	class Measurement
	{
	private:
		TN Data;
	public:
		Measurement(TN data) : Data{ data } {}
		TN value() const
		{
			return Data;
		}
		Measurement& operator+(TN data)
		{
			Data += data;
			return *this;
		}
		bool operator<(const Measurement<TN>& aux)
		{
			return this->Data < aux.Data;
		}
	};

	void main4()
	{
		std::vector<Measurement<int>> v{ 10,2,3 };
		v[2] + 3 + 2; //aduna la masuratoarea 3 valuarea 5
		std::sort(v.begin(), v.end()); //sorteaza masuratorile
		//tipareste masuratorile (in acest caz: 2,8,10)
		for (const auto& m : v)
			std::cout << m.value() << ",";
	}
}

namespace Test3
{
	class Transformer
	{
	public:
		virtual ~Transformer() = default;
		virtual void transform(vector<int>& nrs) = 0;
	};

	class Numbers
	{
	private:
		Transformer* t1;
		vector<int> nrs;
	public:
		Numbers(Transformer* _t1) : t1{ _t1 } {}
		~Numbers() { delete t1; }
		void add(int nr)
		{
			nrs.push_back(nr);
		}

		vector<int>& transform()
		{
			std::sort(nrs.begin(), nrs.end(), [](int a, int b) {return a > b; });
			t1->transform(nrs);
			return nrs;
		}
	};

	class Composite : public Transformer
	{
	private:
		Transformer* t1, * t2;
	public:
		Composite(Transformer* _t1, Transformer* _t2) : t1{ _t1 }, t2{ _t2 }{}
		~Composite() { delete t1; delete t2; }
		void transform(vector<int>& nrs) override
		{
			t1->transform(nrs);
			t2->transform(nrs);
		}
	};

	class Swapper : public Transformer
	{
	public:
		void transform(vector<int>& nrs) override
		{
			for (size_t i = 1; i < nrs.size(); i += 2)
			{
				std::swap(nrs[i - 1], nrs[i]);
			}
		}
	};

	class Adder : public Transformer
	{
	private:
		int cat;
	public:
		Adder(int cat) : cat{ cat } {}
		void transform(vector<int>& nrs) override
		{
			for (auto& el : nrs)
				el += cat;
		}
	};

	class Nuller : public Adder
	{
	public:
		Nuller(int cat) : Adder(cat) {}
		void transform(vector<int>& nrs) override
		{
			Adder::transform(nrs);
			for (auto& el : nrs)
				if (el > 10)
					el = 0;
		}
	};

	Numbers* f()
	{
		Transformer* adder = new Adder(3);
		Transformer* swapper = new Swapper();
		Transformer* nuller = new Nuller(9);
		Transformer* comp = new Composite(adder, swapper);
		comp = new Composite(comp, nuller);
		Numbers* numb = new Numbers(comp);
		return numb;
	}

	void transformerMain()
	{
		Numbers* n1 = f();
		Numbers* n2 = f();
		n1->add(5);
		n1->add(6);
		n1->add(6);
		n1->add(3);
		n1->add(2);

		n2->add(15);
		n2->add(0);
		n2->add(-6);
		n2->add(33);
		n2->add(-10);

		vector<int> aux = n1->transform();
		for (auto& it : aux)
		{
			std::cout << it << " ";
		}
		std::cout << "\n";

		aux = n2->transform();
		for (auto& it : aux)
		{
			std::cout << it << " ";
		}
		std::cout << "\n";
		delete n1;
		delete n2;
	}

	class Examen
	{
	private:
		string Activitate, Ora;
	public:
		Examen(string _Act, string _Ora) : Activitate{ _Act }, Ora{ _Ora }{}
		string getDescriere() const
		{
			return Activitate + " ora " + Ora;
		}
		friend std::ostream& operator << (std::ostream& Os, const Examen& Exam)
		{
			Os << Exam.getDescriere();
			return Os;
		}
	};

	template < class TN >
	class ToDo
	{
	private:
		vector < TN > lista;
	public:
		ToDo<TN>& operator <<(const TN& data)
		{
			lista.push_back(data);
			return *this;
		}

		void printToDoList(std::ostream& Os)
		{
			Os << "De facut:";
			for (auto it = lista.begin(); it != lista.end();it++)
			{
				Os << (*it);
				
				if (it+1 != lista.end())
					Os << ";";
			}
		}
	};

	void todolist() {
		ToDo<Examen> todo;
		Examen oop{ "oop scris","8:00" };
		todo << oop << Examen{ "oop practic", "11:00" }; //Adauga 2 examene la todo
		std::cout << oop.getDescriere() << "\n"; //tipareste la consola: oop scris ora 8:00
		//itereaza elementele adaugate si tipareste la consola lista de activitati
		//in acest caz tipareste: De facut:oop scris ora 8:00;oop practic ora 11:00
		todo.printToDoList(std::cout);
	}
}

namespace Test4
{
	class Channel
	{
	private:

	public:
		virtual void send(string msg) = 0;
		virtual ~Channel() = default;
	};

	class FailOver : public Channel
	{
	private:
		Channel* c[2];
	public:
		FailOver(Channel* _cal1, Channel* _cal2) { c[0] = _cal1; c[1] = _cal2; }
		~FailOver() { delete c[0]; delete c[1]; }
		void send(string msg) override
		{
			for (int i = 0; i < 2; ++i)
			{
				try
				{
					c[i]->send(msg);
					break;
				}
				catch (std::exception& err)
				{
					cout << err.what();
				}
			}
		}
	};

	class Telefon : public Channel
	{
	private:
		int nrTel;
	public:
		Telefon(int nr) : nrTel{ nr }
		{
			srand((unsigned)time(NULL));
		}
		void send(string msg) override
		{
			int nr = rand() % 10;
			if (nr >= 5)
			{
				throw std::exception("Linia este ocupata\n");
			}
			cout << "Dail: " << nrTel << "\n";
		}
	};

	class Fax : public Telefon
	{
	public:
		Fax(int nr) : Telefon{ nr } {}
		void send(string msg) override
		{
			try
			{
				Telefon::send(msg);
				cout << "sending fax\n";
			}
			catch (std::exception& exc)
			{
				cout << exc.what();
			}
		}
	};

	class SMS : public Telefon
	{
	public:
		SMS(int nr) : Telefon{ nr } {}
		void send(string msg) override
		{
			try
			{
				Telefon::send(msg);
				cout << "sending sms\n";
			}
			catch (std::exception exc)
			{
				cout << exc.what();
			}
		}
	};

	class Contact
	{
	private:
		Channel* c[3];
	public:
		Contact(Channel* _c1, Channel* _c2, Channel* _c3) { c[0] = _c1;  c[1] = _c2; c[2] = _c3; }
		~Contact() { delete c[0]; delete c[1]; delete c[2]; }
		void sendAlarm(string msg) 
		{
			for (int i = 0; i < 3; ++i)
			{
				try
				{
					c[i]->send(msg);
					break;
				}
				catch (std::exception& err)
				{
					cout << err.what();
				}
			}
		}
	};

	Contact* f()
	{
		Channel* tel1 = new Telefon{ 12345 };
		Channel* tel2 = new Telefon{ 12345 };
		Channel* fax1 = new Fax{ 312321 };
		Channel* fax2 = new Fax{ 312321 };
		Channel* sms1 = new SMS{ 321 };
		Channel* sms2 = new SMS{ 321 };
		Channel* canal2 = new FailOver{ fax1, sms1 };
		Channel* failover = new FailOver{ fax2, sms2 };
		failover = new FailOver{ tel2, failover };
		Contact* contact = new Contact{ tel1, canal2, failover };

		return contact;
	}

	void main4()
	{
		Contact* ct = f();

		ct->sendAlarm("Mesaj");
		ct->sendAlarm("Daaaa");
		ct->sendAlarm("Brrr");

		delete ct;
	}

	template < class TN >
	class Expresie
	{
	private:
		TN val;
		vector < TN > lista;
	public:
		Expresie(TN data) : val{ data } {}

		Expresie& operator=(const Expresie& alta)
		{
			val = alta.val;
			lista = alta.lista;
			return *this;
		}

		Expresie& operator+(int nr)
		{
			lista.push_back(-nr);
			val += nr;
			return *this;
		}

		Expresie& operator-(int nr)
		{
			lista.push_back(nr);
			val -= nr;
			return *this;
		}

		TN valoare()
		{
			return val;
		}

		Expresie& undo()
		{
			if (!lista.empty())
			{
				val += lista.back();
				lista.pop_back();
			}
			return *this;
		}
	};

	void operatii() {
		Expresie<int> exp{ 3 };//construim o expresie,pornim cu operandul 3
		//se extinde expresia in dreapta cu operator (+ sau -) si operand
		exp = exp + 7 + 3;
		exp = exp - 8;
		//tipareste valoarea expresiei (in acest caz:5 rezultat din 3+7+3-8)
		cout << exp.valoare() << "\n";
		exp.undo(); //reface ultima operatie efectuata
		//tipareste valoarea expresiei (in acest caz:13 rezultat din 3+7+3)
		cout << exp.valoare() << "\n";
		exp.undo().undo();
		cout << exp.valoare() << "\n"; //tipareste valoarea expresiei (in acest caz:3)
	}
}

namespace Test5
{
	class Participant
	{
	public:
		virtual ~Participant() = default;
		virtual void tipareste() = 0;
		virtual bool eVoluntar()
		{
			return true;
		}
	};

	class Personal : public Participant
	{
	private:
		string nume;
	public:
		Personal(string _nume) : nume{ _nume } {}
		void tipareste() override
		{
			cout << nume;
		}
	};

	class Administrator : public Personal
	{
	public:
		Administrator(string nume) : Personal( nume ) {}
		void tipareste() override
		{
			Personal::tipareste();
			cout << " Administrator ";
		}
	};

	class Director : public Personal
	{
	public:
		Director(string nume) : Personal( nume ) {}
		void tipareste() override
		{
			Personal::tipareste();
			cout << " Director ";
		}
	};

	class Angajat : public Participant
	{
	private:
		Participant* p;
	public:
		Angajat(Participant* _p) : p{ _p } {}
		~Angajat() { delete p; }
		void tipareste() override
		{
			p->tipareste();
			cout << " angajat ";
		}
		bool eVoluntar() override
		{
			return false;
		}
	};

	class Ong
	{
	private:
		vector<Participant*> lista;
	public:
		~Ong()
		{
			for (auto& it : lista)
			{
				delete it;
			}
		}

		void add(Participant* p)
		{
			lista.push_back(p);
		}

		vector<Participant*> getAll(bool voluntar)
		{
			vector<Participant*> toRet;

			for (const auto& participant : lista)
			{
				if ((voluntar && participant->eVoluntar()) || (!voluntar && !participant->eVoluntar()))
				{
					toRet.push_back(participant);
				}
			}

			return toRet;
		}
	};

	Ong* f()
	{
		Participant* admin1 = new Administrator{ "Sefu mic, voluntar" };
		Participant* admin2 = new Administrator{ "Vasile, angajat" };
		admin2 = new Angajat(admin2);
		Participant* director1 = new Director{ "Sefu mare, voluntar" };
		Participant* director2 = new Director{ "Vasile mare, angajat" };
		director2 = new Angajat(director2);

		Ong* toRet = new Ong;

		toRet->add(admin1);
		toRet->add(admin2);
		toRet->add(director1);
		toRet->add(director2);

		return toRet;
	}

	void main5()
	{
		Ong* ong = f();

		cout << "Voluntari: ";
		vector<Participant*> aux = ong->getAll(true);

		for (auto& it : aux)
		{
			it->tipareste();
			cout << ";";
		}

		cout << "\nAngajati: ";
		aux = ong->getAll(false);

		for (auto& it : aux)
		{
			it->tipareste();
			cout << ";";
		}

		delete ong;
	}

	template < class T >
	class Cos
	{
	private:
		vector< T > cos;
	public:
		Cos() {}
		Cos(T data) { cos.push_back(data); }

		Cos& operator+(T nou)
		{
			cos.push_back(nou);
			return *this;
		}

		Cos& undo()
		{
			if (!cos.empty())
			{
				cos.pop_back();
			}
			return *this;
		}

		void tipareste(std::ostream& Os)
		{
			for (const auto& produs : cos)
			{
				Os << produs << " ";
			}
		}
	};

	void cumparaturi() {
		Cos<string> cos;//creaza un cos de cumparaturi
		cos = cos + "Mere"; //adauga Mere in cos
		cos.undo();//elimina Mere din cos
		cos + "Mere"; //adauga Mere in cos
		cos = cos + "Paine" + "Lapte";//adauga Paine si Lapte in cos
		cos.undo().undo();//elimina ultimele doua produse adaugate

		cos.tipareste(cout);//tipareste elementele din cos (Mere)
	}
}

namespace Test6
{
	class Mancare
	{
	private:
		int pret;
	public:
		Mancare() { pret = 0; }
		Mancare(int _pret) : pret{ _pret } {}
		virtual ~Mancare() = default;
		virtual string descriere() = 0;
		virtual int getPret()
		{
			return pret;
		}
	};

	class Burger : public Mancare
	{
	private:
		string nume;
	public:
		Burger(string _nume, int _pret) : nume{ _nume }, Mancare( _pret ) {}
		string descriere() override
		{
			return nume;
		}
	};

	class CuCartof : public Mancare
	{
	private:
		Mancare* m;
	public:
		CuCartof(Mancare* _m) : m{ _m } {}
		~CuCartof() { delete m; }
		int getPret() override
		{
			return m->getPret() + 3;
		}
		string descriere() override
		{
			return m->descriere() + " cu cartof";
		}
	};

	class CuSos : public Mancare
	{
	private:
		Mancare* m;
	public:
		CuSos(Mancare* _m) : m{ _m } {}
		~CuSos() { delete m; }
		int getPret() override
		{
			return m->getPret() + 2;
		}
		string descriere() override
		{
			return m->descriere() + " cu sos";
		}
	};

	vector < Mancare* > f()
	{
		vector < Mancare* > list;

		Mancare* McPuisor = new Burger("McPuisor", 10);
		Mancare* BigTasty = new Burger("BigTasty", 15);
		Mancare* Booster1 = new Burger("Booster", 12);
		Mancare* Booster2 = new Burger("Booster", 12);
		BigTasty = new CuCartof(BigTasty);
		BigTasty = new CuSos(BigTasty);
		Booster1 = new CuCartof(Booster1);
		Booster2 = new CuSos(Booster2);

		list.push_back(McPuisor);
		list.push_back(BigTasty);
		list.push_back(Booster1);
		list.push_back(Booster2);

		return list;
	}

	void main6()
	{
		vector < Mancare* > list = f();

		std::sort(list.begin(), list.end(), [](Mancare* m1, Mancare* m2) {return m1->getPret() > m2->getPret(); });

		for (auto& it : list)
		{
			cout << it->descriere() << " " << it->getPret() << "\n";
		}

		for (auto& it : list)
		{
			delete it;
		}
	}

	class Sesiune;
	class Conferinta;

	class Sesiune
	{
	private:
		string Denumire;
		vector < string > Attendants;
	public:
		Sesiune(string nume) : Denumire{ nume } {}

		string getNume()
		{
			return Denumire;
		}

		Sesiune& operator=(Sesiune& ses)
		{
			Denumire = ses.Denumire;
			Attendants = ses.Attendants;
			return *this;
		}
		Sesiune& operator+(string Attendant)
		{
			Attendants.push_back(Attendant);
			return *this;
		}

		vector < string > select(string str)
		{
			vector < string > toRet;
			for (auto& name : Attendants)
			{
				if (name.find(str) == 0)
				{
					toRet.push_back(name);
				}
			}
			return toRet;
		}

		vector < string >::iterator begin()
		{
			return Attendants.begin();
		}

		vector < string >::iterator end()
		{
			return Attendants.end();
		}
	};

	class Conferinta
	{
	private:
		vector < Sesiune > list;
	public:
		Sesiune& track(string nume)
		{
			for (auto& ses : list)
			{
				if (ses.getNume() == nume)
					return ses;
			}
			Sesiune S{ nume };
			list.push_back(S);
			return list.back();
		}
	};

	void man4()
	{
		Conferinta conf;
		//add 2 attendants to "Artifiial Inteligente" track
		conf.track("Artifiial Inteligente") + "Ion Ion" + "Vasile Aron";
		//add 2 attendants to "Software" track
		Sesiune& s = conf.track("Software");
		s + "Anar Lior" + "Aurora Bran";
		//print all attendants from group "Artifiial Inteligente" track
		for (auto name : conf.track("Artifiial Inteligente")) {
			std::cout << name << ",";
		}
		//find and print all names from Software track that contains "ar"
		vector<string> v = conf.track("Software").select("A");
		for (auto name : v) { std::cout << name << ","; }
	}
}

namespace Test7
{
	class Pizza
	{
	private:
		int pret;
	public:
		Pizza() { pret = 0; }
		Pizza(int _pret) : pret{ _pret } {}
		virtual ~Pizza() = default;
		virtual string descriere() = 0;
		virtual int getPret()
		{
			return pret;
		}
	};

	class BasicPizza : public Pizza
	{
	private:
		string nume;
	public:
		BasicPizza(string _nume) : nume{ _nume } {}
		int getPret() override
		{
			if (nume == "Salami")
				return 15;
			return 20;
		}
		string descriere() override
		{
			return nume;
		}
	};

	class PizzacuCiuperci : public Pizza
	{
	private:
		Pizza* m;
	public:
		PizzacuCiuperci(Pizza* _m) : m{ _m } {}
		~PizzacuCiuperci() { delete m; }
		int getPret() override
		{
			return m->getPret() + 3;
		}
		string descriere() override
		{
			return m->descriere() + " cu ciuperci";
		}
	};

	class PizzaCuPeperoni : public Pizza
	{
	private:
		Pizza* m;
	public:
		PizzaCuPeperoni(Pizza* _m) : m{ _m } {}
		~PizzaCuPeperoni() { delete m; }
		int getPret() override
		{
			return m->getPret() + 2;
		}
		string descriere() override
		{
			return m->descriere() + " cu peperoni";
		}
	};

	vector < Pizza* > f()
	{
		vector < Pizza* > list;

		Pizza* salami = new BasicPizza{ "Salami" };
		salami = new PizzacuCiuperci(salami);
		Pizza* salami2 = new BasicPizza{ "Salami" };
		Pizza* diavola = new BasicPizza{ "Diavola" };
		diavola = new PizzaCuPeperoni(diavola);
		diavola = new PizzacuCiuperci(diavola);

		list.push_back(salami);
		list.push_back(salami2);
		list.push_back(diavola);

		return list;
	}

	void main7()
	{
		vector < Pizza* > list = f();

		std::sort(list.begin(), list.end(), [](Pizza* m1, Pizza* m2) {return m1->getPret() > m2->getPret(); });

		for (auto& it : list)
		{
			cout << it->descriere() << " " << it->getPret() << "\n";
		}

		for (auto& it : list)
		{
			delete it;
		}
	}

	template < class T>
	class Catalog
	{
	private:
		string nume;
		vector < T > list;
	public:
		Catalog(string _nume) : nume{ _nume } {}

		Catalog& operator+(T data)
		{
			list.push_back(data);
			return *this;
		}

		class vector < T >::iterator begin()
		{
			return list.begin();
		}
		class vector < T >::iterator end()
		{
			return list.end();
		}
	};

	void catalog() {
		Catalog<int> cat{ "OOP" };//creaza catalog cu note intregi
		cat + 10; //adauga o nota in catalog
		cat = cat + 8 + 6;
		int sum = 0;
		for (auto n : cat) { sum += n; } //itereaza notele din catalog
		std::cout << "Suma note:" << sum << "\n";
	}
}

int main()
{
	{
		Test6::man4();
	}
	_CrtDumpMemoryLeaks();
	return 0;
}