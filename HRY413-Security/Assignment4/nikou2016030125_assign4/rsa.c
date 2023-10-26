#include "rsa.h"
#include "utils.h"

/*
 * Sieve of Eratosthenes Algorithm
 * https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes
 *
 * arg0: A limit
 * arg1: The size of the generated primes list. Empty argument used as ret val
 *
 * ret:  The prime numbers that are less or equal to the limit
 */
size_t *sieve_of_eratosthenes(int limit, int *primes_sz)
{

	size_t *primes;
	int n = limit;
	size_t i = 0;
	size_t c = 0;

	bool list[limit + 1];

	for (i = 0; i < n; i++)
	{
		list[i] = 1;
	}

	for (i = 2; i < sqrt(n); i++)
	{
		if (list[i] == 1)
		{

			int j = i * i;
			while (j < n)
			{
				list[j] = 0;
				j += i;
			}
		}
	}

	for (i = 2; i < n; i++)
	{
		if (list[i] == 1)
		{
			c++;
		}
	}

	primes = (size_t *)malloc(c * sizeof(size_t));

	c = 0;

	for (i = 2; i < n; i++)
	{
		if (list[i] == 1)
		{
			primes[c] = i;
			c++;
		}
	}

	*primes_sz = c;

	return primes;
}

/*
 * Greatest Common Denominator
 *
 * arg0: first number
 * arg1: second number
 *
 * ret: the GCD
 */
int gcd(int a, int b)
{
	a = (a > 0) ? a : -a;
	b = (b > 0) ? b : -b;

	while (a != b)
	{
		if (a > b)
			a -= b;
		else
			b -= a;
	}
	// printf("GCD = %d", a);

	return a;
}

/*
 * Chooses 'e' where
 *     1 < e < fi(n) AND gcd(e, fi(n)) == 1
 *
 * arg0: fi(n)
 *
 * ret: 'e'
 */
size_t
choose_e(size_t fi_n)
{
	size_t e;
	int primes_sz;

	size_t *primes_list = sieve_of_eratosthenes(fi_n, &primes_sz);

	srand(time(0));

	e = primes_list[rand() % primes_sz];
	while ((e % fi_n == 0) && (gcd(e, fi_n) != 1) && (e < fi_n))
	{
		e = primes_list[rand() % (primes_sz + 1)];
	}

	return e;
}

/*
 * Calculates the modular inverse
 *
 * arg0: e^-1
 * arg1: fi_n
 *
 *  solve -> d⋅e ≡ 1 (mod fi_n)
 *
 * ret: modular inverse(d)
 *
 *
 */
size_t
mod_inverse(size_t a, size_t b)
{
	a = a % b;

	for (int i = 1; i < b; i++)
	{
		if (((a * i) % b) == 1)
		{
			return (size_t)i;
		}
	}
}

/*
 * Generates an RSA key pair and saves
 * each key in a different file
 */
void rsa_keygen(void)
{
	size_t p;
	size_t q;
	size_t n;
	size_t fi_n;
	size_t e;
	size_t d;

	int primes_sz;

	size_t *primes = sieve_of_eratosthenes(RSA_SIEVE_LIMIT, &primes_sz);

	srand(time(0));

	do
	{
		p = primes[rand() % primes_sz];

		do
			q = primes[rand() % primes_sz];
		while (p == q);

		n = p * q;

		fi_n = (p - 1) * (q - 1);

	} while (fi_n < 3);

	e = choose_e(fi_n);

	d = mod_inverse(e, fi_n);

	// printf("\np:%ld\tq:%ld\tn:%ld\tfi_n:%ld\te:%ld\td:%ld\n", p, q, n, fi_n, e, d);

	store_keys(n, d, e);
}

/*
 * Encrypts an input file and dumps the ciphertext into an output file
 *
 * arg0: path to input file
 * arg1: path to output file
 * arg2: path to key file
 */
void rsa_encrypt(char *input_file, char *output_file, char *key_file)
{
	unsigned char *plaintext;
	unsigned char *key;
	size_t *c;
	size_t m, n, d;
	int plain_len, key_len;

	plaintext = readFile(input_file, &plain_len);

	key = readFile(key_file, &key_len);

	memcpy(&n, key, sizeof(size_t));
	memcpy(&d, key + sizeof(size_t), sizeof(size_t));

	c = (size_t *)malloc(plain_len * sizeof(size_t));

	for (int i = 0; i < plain_len; i++)
	{
		m = (size_t)plaintext[i];

		c[i] = modular_exp(m, d, n);
	}

	FILE *fp = fopen(output_file, "w");

	fwrite(c, sizeof(size_t), plain_len * sizeof(size_t), fp);

	fclose(fp);
}

/*
 * Decrypts an input file and dumps the plaintext into an output file
 *
 * arg0: path to input file
 * arg1: path to output file
 * arg2: path to key file
 */
void rsa_decrypt(char *input_file, char *output_file, char *key_file)
{
	unsigned char *ciphertext;
	unsigned char *key;
	unsigned char *plaintext;

	// size_t *m;
	size_t *c;
	size_t n, e;
	int cipher_len, key_len;

	ciphertext = readFile(input_file, &cipher_len);

	int num_of_chars = cipher_len / (sizeof(size_t));

	key = readFile(key_file, &key_len);

	memcpy(&n, key, sizeof(size_t));
	memcpy(&e, key + sizeof(size_t), sizeof(size_t));

	c = (size_t *)malloc(cipher_len * sizeof(size_t));
	memcpy(c, ciphertext, num_of_chars);

	plaintext = (unsigned char *)malloc(num_of_chars * sizeof(unsigned char));

	for (int i = 0; i < num_of_chars; i++)
	{
		plaintext[i] = (unsigned char)modular_exp(c[i], e, n);
	}

	FILE *fp = fopen(output_file, "w");

	fwrite(plaintext, sizeof(size_t), num_of_chars, fp);

	fclose(fp);
}

size_t modular_exp(size_t m, size_t d, size_t n)
{
	m = m % n;

	if (m == 0)
		return 0;

	if (d == 0)
		return 1;

	if (d == 1)
		return m;

	if (d % 2 == 0)
	{
		return ((size_t)modular_exp(m * (m % n), d / 2, n) % n);
	}
	else
	{
		return ((size_t)m * modular_exp(m, d - 1, n) % n);
	}
}
