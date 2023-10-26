function [ radiusByScale ] = calcRadiusByScale( levels, k, sigma )
    radiusByScale = zeros(1,levels);
    for i = 1:levels
        radiusByScale(i) =  sqrt(2) * sigma * k^(i-1); 
    end
end