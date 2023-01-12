pkg load statistics

urn = ['rrrrrbbbgg'];

A = 0;
AsiB = 0;
BcondA = 0;

for i=1:5000
  culoare = randsample(urn,3);
  if any(culoare == 'r') %cel putin una e 'r'
    A++;
    if all(culoare == 'r') %toate sunt 'r'
      AsiB++;
    endif
  endif
endfor

A=A/5000
AsiB=AsiB/5000
BcondA = AsiB/A

clf; grid on; hold on;
p=1/3;
n=5;
m=2000;
x=binornd(n,p,1,m);
N=hist(x,0:n);
%sorteaza x si pune de cate ori apar valorile respective in x
bar(0:n,N/m,'hist','FaceColor','b');
bar(0:n,binopdf(0:n,n,p),'FaceColor','y');
legend('probabilitatile estimate','probabilitatile teroretice');
set(findobj('type','patch'),'facealpha',0.7);
xlim([-1 n+1]);
% binopdf -> prob. density f.
% binocdf ->cummultaive density f.

zaruri = randi(6,4,1000);
suma = sum(zaruri);
%sum(zaruri,2) ar face suma pe linii

%posibile 4->24
pos = 4:24;
h = hist(suma, pos);
clf; grid on; hold on;
bar(pos,h/1000,'hist','FaceColor','b');
%bar(pos,binopdf(pos,24,),,'hist','FaceColor','y');

A = [pos',h']

clf; grid on; hold on;
xticks(pos);
xlim([3 25]);
yticks(0:0.01:0.14);
ylim([0 0.14]);
bar(pos,h/1000,'hist','FaceColor','b');

teo=[];
for
  for
    for
      for
        teo=[teo,i1+i2+i3+i4];
      endfor
    endfor
  endfor

