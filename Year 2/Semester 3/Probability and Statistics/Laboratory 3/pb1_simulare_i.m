function PA = pb1_simulare_i(N = 5000)
  PA = 0;
  urn = ['r', 'r', 'r', 'r', 'r', 'b', 'b', 'b', 'g', 'g']
%  urn = 'rrrrrbbbggg'
  for rep = 1 : N
    extragere = randsample(urn, 3);
    if any(extragere == 'r')
      PA++;
    endif
  endfor
  PA = PA / N;
endfunction