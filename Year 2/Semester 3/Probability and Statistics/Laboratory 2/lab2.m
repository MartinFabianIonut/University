count = 0
for step = 1:1000
  if length(unique(randi(365,1,23))) < 23
    count = count + 1;
  end
end

count/1000


clf; hold on;axis square;
rectangle('Position',[0 0 1 1])
nr=0
nr2=0
for i=1:1500
  x = rand; y = rand;
  if min(
    [pdist([x, y; 0.5, 0.5]),...
    pdist([x, y; 1, 1]),...
    pdist([x, y; 0, 0]),...
    pdist([x, y; 0, 1]),...
    pdist([x, y; 1, 0])]) == pdist([x, y; 0.5, 0.5])
    plot(x,y, 'dm','MarkerSize', 2, 'MarkerEdgeColor', 'r');nr2++;
  end
  if ((x-0.5)*(x-0.5) + (y-0.5)*(y-0.5)) <= 0.25
      plot(x,y, 'dm','MarkerSize', 2, 'MarkerEdgeColor', 'g');
      nr++;
  end
end


frecventa_i = nr/1500
frecventa_ii = nr2/1500

bune = 0

for i=1:1500
  obtuz=0;
  x = rand; y = rand;
  AP = pdist([x y; 0 1]);
  BP = pdist([x y; 1 1]);
  CP = pdist([x y; 1 0]);
  DP = pdist([x y; 0 0]);
  if AP^2 + BP^2 < 1
    obtuz++;
  endif
  if BP^2 + CP^2 < 1
    obtuz++;
  endif
  if CP^2 + DP^2 < 1
    obtuz++;
  endif
  if AP^2 + DP^2 < 1
    obtuz++;
  endif
  if obtuz == 2
    bune++;
    if bune == 1
      plot([x y], [1 1]);
      plot([x y], [0 1]);
      plot([x y], [1 0]);
      plot([x y], [0 0]);
    endif
  endif
endfor

frecventa_iii = bune/1500

