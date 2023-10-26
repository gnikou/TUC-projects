monitor: monitor.c
	gcc -O2 -Wall -pedantic monitor.c -o monitor -lpcap


clean:
	rm -f monitor 

run:
	./monitor -r test_pcap_5mins.pcap