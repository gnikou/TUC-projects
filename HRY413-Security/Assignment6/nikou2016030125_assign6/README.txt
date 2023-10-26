Compiled with gcc (Ubuntu 9.3.0-17ubuntu1~20.04) 9.3.0

main: At main function I handle the flags given while calling the program. For flag -h it prints
a help message. For flag -r <filename> it calls packet_process function with filename as argument
and the it prints final statistics at the end before exiting the program. For all other flags an
appropriate error message is shown.

packet_process: With packet capture filename given I call pcap_open_offline to open the file for
reading. Then with pcap_loop function with cnt=0 packets are read until there are no more packets
to read or an error is occured. Then for every packet captured it is called the packet_handler.

packet_handler: The function called by pcap_loop to manage each packet. The function is based on the
format of pcap_handler. To find the payload location I have to find first the header size to properly 
get the pointer to it. At first the pointer is at the packet start. The ethernet header is constant so adding
it to packet pointer I get the IP header pointer. After at IP header I check packet's protocol and if it is
TCP/UDP I handle it below or else I ignore it. Then for TCP packets I call TCP_analysis, with pointers to 
packet and ip_header as args, to analyze its data. Respectively I do this for UDP packets.

TCP_analysis/UDP_analysis: It's the same function for both TCP and UDP with only difference being the
tcp_header/udp_header. At first I get the position of tcp_header adding ip_header to the packet position. The
ip_header is multiplied by 4 cause at these 4 bits stores how many words there are in the header so it has to
be converted to bytes. To get the payload length I subtract from ip_header total length the ip_header length found
before as well as tcp_header length. Lastly I print the data that is stated by exercise. From ip_header I get the
IPv4 addresses and from tcp_header/udp_header I get the ports numbers.

print_statistics(): it is called before exiting the program printing the counters of statistics of all packages.

I implemented the 1-8 steps and step 12(d-h).
For step 9 you can detect a retransmission in TCP packets by looking
for the sequence number of the packet being in order. If a packet number is repeated then it's a retransmission. 
About step 10 there is no retransmission in UDP protocol because of its
fast/connectionless basis.