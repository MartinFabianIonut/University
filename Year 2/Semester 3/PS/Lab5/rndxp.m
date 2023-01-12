% generare folosind metoda inversei

function x = rndxp(lambda, n)

  x = -log(1 - rand(1, n))*lambda;

endfunction
