aproximeaza = 0;
while aproximeaza < 6*pi
    sinusul = sinus(aproximeaza);
    cosinusul = cosinus(aproximeaza);
    
    % Obține fracția de pi
    [numarator, numitor] = rat(aproximeaza/pi);
    if numitor == 1
        frac_text = sprintf('%dpi', numarator);
    else
        frac_text = sprintf('%dpi/%d', numarator, numitor);
    end
    
    fprintf('Valoarea aproximata pentru sin(x) = %f, cos(x) = %f, pentru x = %s\n', sinusul, cosinusul, frac_text);
    fprintf('\tValorile reale pentru sin(x) = %f, cos(x) = %f\n', sin(aproximeaza), cos(aproximeaza));
    aproximeaza = aproximeaza + pi/6;
end